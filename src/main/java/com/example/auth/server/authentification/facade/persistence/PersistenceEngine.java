package com.example.auth.server.authentification.facade.persistence;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.ForbidenDomain;
import com.example.auth.server.authentification.facade.persistence.entities.enums.LogStatus;
import com.example.auth.server.authentification.facade.persistence.entities.logs.UserLog;
import com.example.auth.server.authentification.facade.persistence.repositories.CredentialsRepository;
import com.example.auth.server.authentification.facade.persistence.repositories.ForbidenDomainRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
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
    ForbidenDomainRepository forbidenDomains;


    @Autowired
    PasswordEncoder passwordEncoder;


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

    public void saveAllDomains(Collection<String> domains) {

        Set<String> deja = forbidenDomains.findAll()
                .stream()
                .map(ForbidenDomain::getDomain)
                .collect(Collectors.toSet());

        Set<ForbidenDomain> ds = domains
                .stream()
                .map(String::toLowerCase)
                .filter(d -> !deja.contains(d))
                .map(ForbidenDomain::new)
                .collect(Collectors.toSet());

        forbidenDomains.saveAll(ds);
    }

    public void deleteDomainById(String domain) {
        domain = domain.toLowerCase();
        forbidenDomains.deleteById(domain);
    }

    public void logOnUser(long idUser, LogStatus log) {
        Optional<Credentials> userOpt = userCredentials.findById(idUser);
        if (userOpt.isPresent()) {
            Credentials credentials = userOpt.get();
            UserLog ul = new UserLog(log);
            //ul.setUser(credentials);
            //credentials.getLogs().add(ul);
            //this.saveCredentials(credentials);
        }
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
        userCredentials.deleteAll();
    }

    public void deleteCredentialsById(long iduser) {
        userCredentials.deleteById(iduser);
    }


    public Collection<Credentials> findCredentialsByDomainMail(String domain) {
        return userCredentials.findAll()
                .stream()
                .filter(c -> c.getMail().split("@")[1].equals(domain))
                .collect(Collectors.toSet());
    }

    public Collection<Credentials> findCredentialsWithFordindenDomainMail() {
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


}
