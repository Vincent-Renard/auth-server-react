package com.example.auth.server.authentification.facade;

import com.example.auth.server.authentification.facade.persistence.LogsEngine;
import com.example.auth.server.authentification.facade.persistence.PersistenceEngine;
import com.example.auth.server.authentification.facade.persistence.entities.Banishment;
import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.ForbidenDomain;
import com.example.auth.server.authentification.facade.persistence.entities.enums.BanReason;
import com.example.auth.server.authentification.facade.pojos.UserToken;
import com.example.auth.server.authentification.token.manager.JwtDecoder;
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
    PersistenceEngine base;

    @Autowired
    LogsEngine logsEngine;

    @Autowired
    JwtEncoder tokenEncoder;

    @Autowired
    JwtDecoder tokenDecoder;

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

        if (!base.credentialsExistsWithMail(mailAdmin)) {

            var admin = new Credentials(mailAdmin, base.encodePassword(password), Set.of("USER", "ADMIN"));
            base.saveCredentials(admin);
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

        p.setForbidenDomains(base.findAllDomains().stream().map(ForbidenDomain::getDomain).collect(Collectors.toSet()));
        p.setKey(tokenEncoder.getPublicKey().getEncoded());
        Collection<Credentials> credentials = base.findAllCredentials();
        p.setNbUsersRegistered(credentials.size());
        p.setNbUsersAdminsRegistered(credentials.stream().filter(c -> c.getRoles().contains("ADMIN")).count());

        return p;
    }


    @Override
    public void addForbidenDomain(String domain) {
        domain = domain.toLowerCase();
        base.saveDomain(new ForbidenDomain(domain));
    }

    @Override
    public void addForbidenDomains(Collection<String> domains) {

        base.saveAllDomains(domains);


    }

    @Override
    public void delForbidenDomain(String domain) {
        base.deleteDomainById(domain);

    }

    @Override
    public Collection<ForbidenDomain> getAllDomainNotAllowed() {

        return base.findAllDomains();
    }

    @Override
    public Collection<Credentials> getAllUsersWithDomainsNotAllowed() {

        return base.findCredentialsWithFordindenDomainMail();
    }


    @Override
    public UserToken signIn(String mailUser, String passsword) throws MailAlreadyTakenException, BadPasswordFormat, InvalidMail, ForbidenDomainMailUse, UserBan {
        if (!passwordChecker.test(passsword))
            throw new BadPasswordFormat();
        if (!mailChecker.test(mailUser))
            throw new InvalidMail();
        String domain = mailUser.split("@")[1];
        if (base.existDomainByName(domain))
            throw new ForbidenDomainMailUse();
        Optional<Credentials> c = base.findCredentialsByMail(mailUser);

        if (c.isPresent()) {
            if (c.get().getBanishment() != null)
                throw new UserBan();
            throw new MailAlreadyTakenException();
        }


        Set<String> rolesOfUser = new TreeSet<>(BASE_ROLES);

        var credentials = new Credentials(mailUser, base.encodePassword(passsword), rolesOfUser);


        var user = base.saveCredentials(credentials);
        logsEngine.logRegistration(user);
        Bearers tokens = tokenEncoder.genBoth(user.getIdUser(), user.getRoles());
        return new UserToken(user.getIdUser(), tokens);
    }

    @Override
    public Bearers logIn(String mail, String password) throws BadPasswordException, NotSuchUserException, UserBan {
        Optional<Credentials> optCredentials = base.findCredentialsByMail(mail);
        if (optCredentials.isPresent()) {
            var credentials = optCredentials.get();
            if (credentials.getBanishment() != null)
                throw new UserBan();
            if (!base.passwordMatches(password, credentials.getPassword())) {
                logsEngine.logBadPassword(credentials);
                throw new BadPasswordException();
            }
            logsEngine.logLogin(credentials);

            return tokenEncoder.genBoth(credentials.getIdUser(), credentials.getRoles());
        } else throw new NotSuchUserException();

    }

    @Override
    public Bearers refresh(String token) throws NotSuchUserException, UserBan, NoToken, InvalidToken, TokenExpired {
        long idUser = tokenDecoder.decodeRefreshToken(token);
        Optional<Credentials> optCredentials = base.findCredentialsById(idUser);
        if (optCredentials.isPresent()) {
            Credentials credentials = optCredentials.get();
            if (credentials.getBanishment() != null) {
                throw new UserBan();
            }

            //TODO base.logOnUser(credentials.getIdUser(), LogStatus.REFRESHING);
            logsEngine.LogRefreshing(credentials);
            return tokenEncoder.genBoth(idUser, credentials.getRoles());

        } else {
            throw new NotSuchUserException();
        }

    }

    @Override
    public void clear() {
        base.deleteAllCredentials();
        fill();
    }

    @Override
    public void signOut(long idUser) throws NotSuchUserException, UserBan {
        Optional<Credentials> optCredentials = base.findCredentialsById(idUser);
        if (optCredentials.isPresent()) {
            var credentials = optCredentials.get();
            if (credentials.getBanishment() != null)
                throw new UserBan();

            base.deleteCredentialsById(idUser);

        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public Credentials updateRoles(long idUser, Collection<String> newRoles, long idAdmin) throws NotSuchUserException, NotSuchAdminException {
        Optional<Credentials> optCredentialsUser = base.findCredentialsById(idUser);
        Optional<Credentials> optCredentialsAdmin = base.findCredentialsById(idAdmin);
        newRoles = newRoles.stream().map(String::toUpperCase).collect(Collectors.toSet());

        if (!optCredentialsUser.isPresent()) {
            throw new NotSuchUserException();
        }
        if (!optCredentialsAdmin.isPresent()) {
            throw new NotSuchAdminException();
        }
        var userCredentials = optCredentialsUser.get();
        if (POSSILBES_ROLES.containsAll(newRoles)) {

            var adminCredentials = optCredentialsAdmin.get();
            if (!newRoles.equals(userCredentials.getRoles())) {
                userCredentials.setRoles(newRoles);
                userCredentials = base.saveCredentials(userCredentials);
                logsEngine.logRoleUpdate(userCredentials, adminCredentials, newRoles);
            }

            return userCredentials;


        }
        return userCredentials;
    }

    @Override
    public Credentials showUser(long idUser) throws NotSuchUserException {
        Optional<Credentials> optCredentials = base.findCredentialsById(idUser);
        if (optCredentials.isPresent()) {
            return optCredentials.get();
        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public Collection<Credentials> getAllUsersWithDomain(String domain) {
        domain = domain.toLowerCase();
        return base.findCredentialsByDomainMail(domain);
    }

    @Override
    public Collection<Credentials> getAllUsersWithRole(String role) {
        role = role.toUpperCase();

        if (POSSILBES_ROLES.contains(role)) {
            return base.getUsersWithRole(role);
        }

        return new HashSet<>();
    }

    @Override
    public void updatePassword(long idUser, String newpasssword) throws NotSuchUserException, BadPasswordFormat, UserBan {
        Optional<Credentials> optCredentials = base.findCredentialsById(idUser);

        if (optCredentials.isPresent()) {
            var credentials = optCredentials.get();
            if (credentials.getBanishment() != null)
                throw new UserBan();
            if (!passwordChecker.test(newpasssword)) throw new BadPasswordFormat();

            //base.logOnUser(credentials.getIdUser(), LogStatus.UPDATE_PASSWORD);
            credentials.setPassword(base.encodePassword(newpasssword));

            base.saveCredentials(credentials);
            logsEngine.logUpdatePassword(credentials);
        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public void updateMail(long idUser, String newmail) throws MailAlreadyTakenException, NotSuchUserException, InvalidMail, ForbidenDomainMailUse, UserBan {
        Optional<Credentials> optCredentials = base.findCredentialsById(idUser);

        if (optCredentials.isPresent()) {

            var credentials = optCredentials.get();
            if (credentials.getBanishment() != null)
                throw new UserBan();

            if (!mailChecker.test(newmail))
                throw new InvalidMail();
            String domain = newmail.split("@")[1];
            if (base.existDomainByName(domain))
                throw new ForbidenDomainMailUse();

            if (base.credentialsExistsWithMail(newmail))
                throw new MailAlreadyTakenException();

            if (credentials.getMail().equals(mailAdmin))
                newmail = mailAdmin;

            String oldMail = credentials.getMail();
            credentials.setMail(newmail);
            base.saveCredentials(credentials);
            // base.logOnUser(credentials.getIdUser(), LogStatus.UPDATE_MAIL);
            logsEngine.logMailUpdate(oldMail, credentials);
        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public Credentials banUser(long idUser, BanReason reason, long idAdmin) throws NotSuchUserException, UserAlreadyBanException {
        Optional<Credentials> optCredentials = base.findCredentialsById(idUser);
        Optional<Credentials> optAdmin = base.findCredentialsById(idAdmin);
        if (optCredentials.isPresent() && optAdmin.isPresent()) {
            var admin = optAdmin.get();
            var credentials = optCredentials.get();

            if (credentials.getBanishment() != null) throw new UserAlreadyBanException();

            Banishment be = new Banishment(reason);
            credentials.setBanishment(be);
            if (!credentials.getMail().equals(mailAdmin)) {
                base.saveCredentials(credentials);
                logsEngine.logBan(credentials, admin, reason);

            }

            return credentials;
        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public void unBanUser(long idUser, long idAdmin) throws NotSuchUserException {
        Optional<Credentials> optUser = base.findCredentialsById(idUser);
        Optional<Credentials> optAdmin = base.findCredentialsById(idAdmin);
        if (optUser.isPresent() && optAdmin.isPresent()) {

            var user = optUser.get();
            var admin = optAdmin.get();
            user.setBanishment(null);
            base.saveCredentials(user);
            logsEngine.logUnban(user, admin);

        } else {
            throw new NotSuchUserException();
        }
    }
}