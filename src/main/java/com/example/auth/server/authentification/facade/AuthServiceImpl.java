package com.example.auth.server.authentification.facade;

import com.example.auth.server.authentification.facade.persistence.entities.BanReason;
import com.example.auth.server.authentification.facade.persistence.entities.Banishment;
import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.ForbidenDomain;
import com.example.auth.server.authentification.facade.persistence.repositories.CredentialsRepository;
import com.example.auth.server.authentification.facade.persistence.repositories.ForbidenDomainRepository;
import com.example.auth.server.authentification.facade.pojos.UserToken;
import com.example.auth.server.authentification.token.manager.JwtEncoder;
import com.example.auth.server.model.dtos.out.AuthServerStateAdmin;
import com.example.auth.server.model.dtos.out.AuthServerStatePublic;
import com.example.auth.server.model.dtos.out.Bearers;
import com.example.auth.server.model.exceptions.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @autor Vincent
 * @date 12/03/2020
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService, AuthUtils {

    static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    final String mailAdmin = "admin@admin.com";

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CredentialsRepository credentialsRepository;

    @Autowired
    ForbidenDomainRepository domainRepository;

    @Autowired
    JwtEncoder tokenEncoder;

    LocalDateTime startDate;

    @PostConstruct
    private void init() {
        startDate = LocalDateTime.now();
        fill();

    }



    private String genPassword(int lenght, int chunks) {
        String sep = "-";
        String alphaU = "ABCDEGHIJKLMNOPQRSTUVWXYZ";
        String alpha = alphaU + alphaU.toLowerCase() + "0123456789";
        alpha = alpha.replace("0", "")
                .replace("O", "");
        StringBuilder s = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i <= lenght; i++) {
            s.append(alpha.charAt(r.nextInt(alpha.length())));
            if (i > 0 && i % chunks == 0 && i < lenght) {
                s.append(sep);
            }

        }
        //return s.toString();
        return "pTWER-gYG5-ohom-qsZg";
    }

    private void fill() {
        var password = genPassword(16, 4);

        if (!credentialsRepository.existsByMail(mailAdmin)) {

            var admin = new Credentials(mailAdmin, passwordEncoder.encode(password), Set.of("USER", "ADMIN"));
            credentialsRepository.save(admin);
            logger.info(mailAdmin + "  " + password);
        }
    }

    @Override
    public PublicKey publicKey() {
        return tokenEncoder.getPublicKey();
    }

    @Override
    public AuthServerStatePublic getServerStatePublic() {
        AuthServerStatePublic p = new AuthServerStatePublic();

        p.setAuthTokenTTL(tokenEncoder.getAuthTTL());
        p.setRefreshTokenTTL(tokenEncoder.getRefreshTTL());
        p.setStartDateServer(startDate);
        p.setKey(tokenEncoder.getPublicKey().getEncoded());

        return p;
    }

    @Override
    public AuthServerStateAdmin getServerStateAdmin() {
        AuthServerStateAdmin p = new AuthServerStateAdmin();

        p.setAuthTokenTTL(tokenEncoder.getAuthTTL());
        p.setRefreshTokenTTL(tokenEncoder.getRefreshTTL());
        p.setStartDateServer(startDate);

        p.setForbidenDomains(domainRepository.findAll().stream().map(ForbidenDomain::getDomain).collect(Collectors.toSet()));
        p.setKey(tokenEncoder.getPublicKey().getEncoded());
        Collection<Credentials> credentials = credentialsRepository.findAll();
        p.setNbUsersRegistered(credentials.size());
        p.setNbUsersAdminsRegistered(credentials.stream().filter(c -> c.getRoles().contains("ADMIN")).count());

        return p;
    }


    @Override
    public void addForbidenDomain(String domain) {
        domain = domain.toLowerCase();

        domainRepository.save(new ForbidenDomain(domain));


    }

    @Override
    public void addForbidenDomains(Collection<String> domains) {
        Set<String> deja = domainRepository.findAll()
                .stream()
                .map(ForbidenDomain::getDomain)
                .collect(Collectors.toSet());

        Set<ForbidenDomain> ds = domains
                .stream()
                .map(String::toLowerCase)
                .filter(d -> !deja.contains(d))
                .map(ForbidenDomain::new)
                .collect(Collectors.toSet());
        domainRepository.saveAll(ds);


    }

    @Override
    public void delForbidenDomain(String domain) {
        domain = domain.toLowerCase();
        domainRepository.deleteById(domain);

    }

    @Override
    public Collection<ForbidenDomain> getAllDomainNotAllowed() {

        return domainRepository.findAll();
    }

    @Override
    public UserToken signIn(String mailUser, String passsword) throws MailAlreadyTakenException, BadPasswordFormat, InvalidMail, ForbidenDomainMailUse, UserBan {
        mailUser = mailUser.toLowerCase();
        if (!passwordChecker.test(passsword))
            throw new BadPasswordFormat();
        if (!mailChecker.test(mailUser))
            throw new InvalidMail();
        String domain = mailUser.split("@")[1];
        if (domainRepository.existsByDomain(domain))
            throw new ForbidenDomainMailUse();
        Optional<Credentials> c = credentialsRepository.findByMail(mailUser);

        if (c.isPresent()) {
            if (c.get().getBanishment() != null)
                throw new UserBan();
            throw new MailAlreadyTakenException();
        }


        Set<String> rolesOfUser = new TreeSet<>(BASE_ROLES);

        var credentials = new Credentials(mailUser, passwordEncoder.encode(passsword), rolesOfUser);
        var user = credentialsRepository.save(credentials);

        Bearers tokens = tokenEncoder.genBoth(user.getIdUser(), user.getRoles());
        return new UserToken(user.getIdUser(), tokens);
    }

    @Override
    public Bearers logIn(String mail, String passsword) throws BadPasswordException, NotSuchUserException, UserBan {
        mail = mail.toLowerCase();
        Optional<Credentials> u = credentialsRepository.findByMail(mail);
        if (u.isPresent()) {
            var user = u.get();
            if (user.getBanishment() != null)
                throw new UserBan();
            if (!passwordEncoder.matches(passsword, user.getPassword()))
                throw new BadPasswordException();

            return tokenEncoder.genBoth(user.getIdUser(), user.getRoles());
        } else throw new NotSuchUserException();

    }

    @Override
    public Bearers refresh(long iduser) throws NotSuchUserException, UserBan {

        Optional<Credentials> user = credentialsRepository.findById(iduser);
        if (user.isPresent()) {
            if (user.get().getBanishment() != null)
                throw new UserBan();
            return tokenEncoder.genBoth(iduser, user.get().getRoles());

        } else {
            throw new NotSuchUserException();
        }

    }

    @Override
    public void clear() {
        credentialsRepository.deleteAll();
        fill();
    }

    @Override
    public void signOut(long iduser, String password) throws BadPasswordException, NotSuchUserException, UserBan {
        Optional<Credentials> user = credentialsRepository.findById(iduser);
        if (user.isPresent()) {
            var usr = user.get();
            if (usr.getBanishment() != null)
                throw new UserBan();
            if (passwordEncoder.matches(password, usr.getPassword()))
                credentialsRepository.deleteById(iduser);
            else throw new BadPasswordException();

        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public Credentials updateRoles(long iduser, Collection<String> newRoles) throws NotSuchUserException {
        Optional<Credentials> user = credentialsRepository.findById(iduser);
        newRoles = newRoles.stream().map(String::toUpperCase).collect(Collectors.toSet());
        if (!user.isPresent()) {
            throw new NotSuchUserException();
        } else {
            if (POSSILBES_ROLES.containsAll(newRoles)) {
                var usr = user.get();
                usr.setRoles(newRoles);
                usr = credentialsRepository.save(usr);
                return usr;

            }
        }
        return user.get();
    }

    @Override
    public Credentials showUser(long iduser) throws NotSuchUserException {
        Optional<Credentials> user = credentialsRepository.findById(iduser);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public void updatePassword(long iduser, String oldPasssword, String newpasssword) throws NotSuchUserException, BadPasswordException, BadPasswordFormat, UserBan {
        Optional<Credentials> user = credentialsRepository.findById(iduser);

        if (user.isPresent()) {
            var usr = user.get();
            if (usr.getBanishment() != null)
                throw new UserBan();
            if (!passwordChecker.test(newpasssword)) throw new BadPasswordFormat();
            if (passwordEncoder.matches(oldPasssword, usr.getPassword())) {
                usr.setPassword(passwordEncoder.encode(newpasssword));
                credentialsRepository.save(usr);
            } else throw new BadPasswordException();
        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public void updateMail(long iduser, String password, String newmail) throws MailAlreadyTakenException, NotSuchUserException, InvalidMail, BadPasswordException, ForbidenDomainMailUse, UserBan {
        Optional<Credentials> user = credentialsRepository.findById(iduser);

        if (user.isPresent()) {

            newmail = newmail.toLowerCase();
            var usr = user.get();
            if (usr.getBanishment() != null)
                throw new UserBan();
            if (!passwordEncoder.matches(password, usr.getPassword()))
                throw new BadPasswordException();
            if (!mailChecker.test(newmail))
                throw new InvalidMail();
            String domain = newmail.split("@")[1];
            if (domainRepository.existsByDomain(domain))
                throw new ForbidenDomainMailUse();

            if (credentialsRepository.existsByMail(newmail))
                throw new MailAlreadyTakenException();

            if (usr.getMail().equals(mailAdmin))
                newmail = mailAdmin;

            usr.setMail(newmail);
            credentialsRepository.save(usr);

        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public Credentials banUser(long idUser, BanReason reason, long idAdmin) throws NotSuchUserException, UserAlreadyBanException {
        Optional<Credentials> user = credentialsRepository.findById(idUser);

        if (user.isPresent()) {
            var usr = user.get();
            long i = usr.getIdUser();
            if (usr.getBanishment() != null) throw new UserAlreadyBanException();

            Banishment be = new Banishment(reason);
            usr.setBanishment(be);
            if (!usr.getMail().equals(mailAdmin))
                credentialsRepository.save(usr);
            return usr;
        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public void unBanUser(long idUser, long idAdmin) throws NotSuchUserException {
        Optional<Credentials> user = credentialsRepository.findById(idUser);

        if (user.isPresent()) {

            var usr = user.get();
            usr.setBanishment(null);
            credentialsRepository.save(usr);

        } else {
            throw new NotSuchUserException();
        }
    }
}
