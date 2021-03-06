package com.example.auth.server.authentification.facade.persistence;

import com.example.auth.server.authentification.facade.persistence.entities.Banishment;
import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.ForbidenDomain;
import com.example.auth.server.authentification.facade.persistence.entities.ResetPasswordToken;
import com.example.auth.server.authentification.facade.persistence.repositories.BanishmentRepository;
import com.example.auth.server.authentification.facade.persistence.repositories.CredentialsRepository;
import com.example.auth.server.authentification.facade.persistence.repositories.ForbidenDomainRepository;
import com.example.auth.server.authentification.facade.persistence.repositories.ResetPasswordTokenRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @autor Vincent
 * @date 28/07/2020
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersistenceEngine {

    @Autowired
    CredentialsRepository userCredentials;

    @Autowired
    BanishmentRepository bans;

    @Autowired
    ForbidenDomainRepository forbidenDomains;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    LogsEngine logsEngine;


    @Autowired
    ResetPasswordTokenRepository resetPasswordTokens;

    public String encodePassword(String clearPassword) {
        return passwordEncoder.encode(clearPassword);
    }


    public boolean credentialsExistsWithMail(String mail) {
        mail = mail.toLowerCase();
        return userCredentials.existsByMail(mail);
    }

    public Collection<Credentials> findAllCredentials() {
        return userCredentials.findAll();
    }

    public Credentials saveCredentials(Credentials credentials) {
        return userCredentials.save(credentials);
    }

    public Collection<ForbidenDomain> findAllDomains() {
        return forbidenDomains.findAll();
    }


    public ForbidenDomain saveDomain(ForbidenDomain forbidenDomain) {
        return forbidenDomains.save(forbidenDomain);
    }

    public void saveDomains(Credentials admin, Collection<String> domains) {

        Set<String> deja = forbidenDomains.findAll()
                .stream()
                .map(ForbidenDomain::getDomain)
                .collect(Collectors.toSet());

        Set<ForbidenDomain> ds = domains
                .stream()
                .map(String::toLowerCase)
                .filter(d -> !deja.contains(d))
                .map(d -> new ForbidenDomain(admin, d))
                .collect(Collectors.toSet());
        ds.forEach(forbidenDomain -> logsEngine.logBanDomain(forbidenDomain.getAdmin(), forbidenDomain.getDomain()));
        forbidenDomains.saveAll(ds);
    }

    public void deleteDomainById(String domain) {
        domain = domain.toLowerCase();
        forbidenDomains.deleteById(domain);
    }


    public Optional<Credentials> findCredentialsById(long idUser) {
        return userCredentials.findById(idUser);
    }

    public boolean existDomainByName(String domain) {
        domain = domain.toLowerCase();
        return forbidenDomains.existsByDomain(domain);
    }

    public Optional<Credentials> findCredentialsByMail(String mailUser) {
        mailUser = mailUser.toLowerCase();
        return userCredentials.findByMail(mailUser);
    }

    public boolean passwordMatches(String clearPassword, String encodedPassword) {
        return passwordEncoder.matches(clearPassword, encodedPassword);
    }

    public void deleteAllCredentials() {
        userCredentials.findAll().forEach(c -> deleteCredentialsById(c.getIdUser()));
    }

    public void deleteCredentialsById(long iduser) {
        var opt = userCredentials.findById(iduser);
        if (opt.isPresent()) {
            var cred = opt.get();
            if (cred.getRoles().contains("ADMIN")) {
                var listBansByAdmin = bans.findBanishmentByAdmin_IdUser(iduser);
                listBansByAdmin.forEach(banishment -> banishment.setAdmin(null));
                bans.saveAll(listBansByAdmin);
                logsEngine.delogAdmin(iduser);


            }
            logsEngine.delogUser(iduser);
            var ltokens = resetPasswordTokens.findAllByUserIdUser(iduser);
            ltokens.forEach(t -> t.setUser(null));
            resetPasswordTokens.saveAll(ltokens);
            var ids = ltokens.stream().map(ResetPasswordToken::getResetToken);
            ids.forEach(id -> resetPasswordTokens.deleteById(id));

            userCredentials.deleteById(iduser);

        }

    }


    public Collection<Credentials> findCredentialsByDomainMail(String domain) {
        return userCredentials.findAll()
                .stream()
                .filter(c -> c.getMail().split("@")[1].equals(domain))
                .collect(Collectors.toSet());
    }

    public Collection<Credentials> findCredentialsWithForbiddenDomainMail() {
        Set<String> fds = forbidenDomains.findAll().stream().map(ForbidenDomain::getDomain).collect(Collectors.toSet());
        return userCredentials.findAll()
                .stream()
                .filter(uc -> fds.contains(uc.getMail().split("@")[1]))
                .collect(Collectors.toSet());
    }

    public Collection<Credentials> getUsersWithRole(String role) {
        return userCredentials.findAll().stream()
                .filter(uc -> uc.getRoles().contains(role))
                .collect(Collectors.toSet());
    }


    public void deleteBanishment(Banishment banishment) {
        var u = banishment.getUser();
        banishment.setUser(null);
        banishment.setAdmin(null);
        bans.save(banishment);
        u.setBanishment(null);
        userCredentials.save(u);

    }

    public ResetPasswordToken generateResetToken(Credentials u) {

        String key;
        do {
            key = UUID.randomUUID().toString().replace("-", "");
        } while (resetPasswordTokens.existsByResetToken(key));
        ResetPasswordToken rpt = new ResetPasswordToken(key, u);
        rpt = resetPasswordTokens.save(rpt);

        return rpt;
    }


    public Optional<ResetPasswordToken> findToken(String key) {
        return resetPasswordTokens.findById(key);
    }

    public void cleanOldAndUnusedResetTokens(int ttl) {
        /*
       var td= resetPasswordTokens.findAll().stream()
                .filter(rpt-> rpt.getDateTime()
                        .plusSeconds(ttl)
                        .isBefore(LocalDateTime.now())
                        && rpt.getDateTimeUse()==null)
               .collect(Collectors.toList()).forEach(rpt -> rpt.setUser);
       */
    }

    // #{      @}
    public void useToken(ResetPasswordToken token) {
        token.use();
        resetPasswordTokens.save(token);
    }
}
