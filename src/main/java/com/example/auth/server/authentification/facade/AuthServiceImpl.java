package com.example.auth.server.authentification.facade;

import com.example.auth.server.authentification.facade.persistence.LogsEngine;
import com.example.auth.server.authentification.facade.persistence.PersistenceEngine;
import com.example.auth.server.authentification.facade.persistence.entities.Banishment;
import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.ForbidenDomain;
import com.example.auth.server.authentification.facade.persistence.entities.ResetPasswordToken;
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
    public AuthServerStateAdmin getServerStateAdmin(long idAdmin) throws NotSuchUserException {

        Optional<Credentials> optAdmin = base.findCredentialsById(idAdmin);
        if (optAdmin.isPresent()) {
            AuthServerStateAdmin p = new AuthServerStateAdmin();

            p.setAuthTokenTTL(tokenEncoder.getAuthTTL());
            p.setRefreshTokenTTL(tokenEncoder.getRefreshTTL());
            p.setStartDateServer(startDate);

            p.setForbidenDomains(base.findAllDomains().stream().map(ForbidenDomain::getDomain).collect(Collectors.toSet()));
            p.setKey(tokenEncoder.getPublicKey().getEncoded());
            Collection<Credentials> credentials = base.findAllCredentials();
            p.setNbUsersRegistered(credentials.size());
            p.setNbUsersAdminsRegistered(credentials.stream().filter(c -> c.getRoles().contains("ADMIN")).count());
            var admin = optAdmin.get();
            logsEngine.logAsksServerStateAdmin(admin);
            return p;
        } else throw new NotSuchUserException();

    }


    @Override
    public void addForbidenDomain(long idAdmin, String domain) {
        var optAdmin = base.findCredentialsById(idAdmin);
        if (optAdmin.isPresent()) {
            var admin = optAdmin.get();
            domain = domain.toLowerCase();
            base.saveDomain(new ForbidenDomain(admin, domain));
            logsEngine.logBanDomain(admin, domain);
        }

    }

    @Override
    public void addForbidenDomains(long idAdmin, Collection<String> domains) {
        Optional<Credentials> optAdmin = base.findCredentialsById(idAdmin);
        optAdmin.ifPresent(credentials -> base.saveDomains(credentials, domains));


    }

    @Override
    public void delForbidenDomain(long idAdmin, String domain) {
        base.deleteDomainById(domain);

        logsEngine.logUnbanDomain(idAdmin, domain);

    }

    @Override
    public Collection<ForbidenDomain> getAllDomainNotAllowed() {

        return base.findAllDomains();
    }

    @Override
    public Collection<Credentials> getAllUsersWithDomainsNotAllowed() {

        return base.findCredentialsWithForbiddenDomainMail();
    }


    @Override
    public UserToken signIn(String mailUser, String passsword) throws MailAlreadyTakenException, BadPasswordFormatException, InvalidMailException, ForbiddenDomainMailUseException, UserBanException {
        if (!passwordChecker.test(passsword))
            throw new BadPasswordFormatException();
        if (!mailChecker.test(mailUser))
            throw new InvalidMailException();
        String domain = mailUser.split("@")[1];
        if (base.existDomainByName(domain))
            throw new ForbiddenDomainMailUseException();
        Optional<Credentials> c = base.findCredentialsByMail(mailUser);

        if (c.isPresent()) {
            if (c.get().getBanishment() != null)
                throw new UserBanException();
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
    public Bearers logIn(String mail, String password) throws BadPasswordException, NotSuchUserException, UserBanException {
        Optional<Credentials> optCredentials = base.findCredentialsByMail(mail);
        if (optCredentials.isPresent()) {
            var credentials = optCredentials.get();


            if (!base.passwordMatches(password, credentials.getPassword())) {
                logsEngine.logBadPassword(credentials);
                throw new BadPasswordException();
            }
            if (credentials.getBanishment() != null) {
                throw new UserBanException();
            }

            logsEngine.logLogin(credentials);
            return tokenEncoder.genBoth(credentials.getIdUser(), credentials.getRoles());
        } else throw new NotSuchUserException();

    }

    @Override
    public Bearers refresh(String token) throws NotSuchUserException, UserBanException, NoTokenException, InvalidTokenException, TokenExpiredException {
        long idUser = tokenDecoder.decodeRefreshToken(token);
        Optional<Credentials> optCredentials = base.findCredentialsById(idUser);
        if (optCredentials.isPresent()) {
            Credentials credentials = optCredentials.get();
            if (credentials.getBanishment() != null) {
                throw new UserBanException();
            }

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
    public void signOut(long idUser) throws NotSuchUserException, UserBanException {
        Optional<Credentials> optCredentials = base.findCredentialsById(idUser);
        if (optCredentials.isPresent()) {
            var credentials = optCredentials.get();
            if (credentials.getBanishment() != null)
                throw new UserBanException();

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
    public Credentials showUser(long idAdmin, long idUser) throws NotSuchUserException {
        Optional<Credentials> optCredentials = base.findCredentialsById(idAdmin);
        if (optCredentials.isPresent()) {
            return showUser(idUser);
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
    public ResetPasswordToken askResetPasswordToken(String mail) throws NotSuchUserException, UserBanException {
        var optUser = base.findCredentialsByMail(mail);
        if (optUser.isPresent()) {
            var u = optUser.get();
            if (u.getBanishment() != null)
                throw new UserBanException();

            logsEngine.askResetPasswordToken(u);
            return base.generateResetToken(u);
        } else throw new NotSuchUserException();

    }

    @Override
    public void useResetPasswordToken(String key, String newPassword) throws UserBanException, BadPasswordFormatException, TokenNotFoundException {
        var optToken = base.findToken(key);
        if (optToken.isPresent()) {
            var token = optToken.get();
            var u = token.getUser();
            if (u.getBanishment() != null)
                throw new UserBanException();
            if (!passwordChecker.test(newPassword)) throw new BadPasswordFormatException();

            u.setPassword(base.encodePassword(newPassword));


            base.useToken(token);
            base.saveCredentials(u);
            logsEngine.logUpdatePasswordByResetToken(u);
            base.cleanOldAndUnusedResetTokens(TTL_SECONDS_RESET_PASSWORD_TOKEN);
        } else throw new TokenNotFoundException();
    }


    @Override
    public void updatePassword(long idUser, String newpasssword) throws NotSuchUserException, BadPasswordFormatException, UserBanException {
        Optional<Credentials> optCredentials = base.findCredentialsById(idUser);

        if (optCredentials.isPresent()) {
            var credentials = optCredentials.get();
            if (credentials.getBanishment() != null)
                throw new UserBanException();
            if (!passwordChecker.test(newpasssword)) throw new BadPasswordFormatException();

            credentials.setPassword(base.encodePassword(newpasssword));

            base.saveCredentials(credentials);
            logsEngine.logUpdatePassword(credentials);
        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public void updateMail(long idUser, String newmail) throws MailAlreadyTakenException, NotSuchUserException, InvalidMailException, ForbiddenDomainMailUseException, UserBanException {
        Optional<Credentials> optCredentials = base.findCredentialsById(idUser);

        if (optCredentials.isPresent()) {

            var credentials = optCredentials.get();
            if (credentials.getBanishment() != null)
                throw new UserBanException();

            if (!mailChecker.test(newmail))
                throw new InvalidMailException();
            String domain = newmail.split("@")[1];
            if (base.existDomainByName(domain))
                throw new ForbiddenDomainMailUseException();

            if (base.credentialsExistsWithMail(newmail))
                throw new MailAlreadyTakenException();

            if (credentials.getMail().equals(mailAdmin))
                newmail = mailAdmin;

            String oldMail = credentials.getMail();
            credentials.setMail(newmail);
            base.saveCredentials(credentials);
            logsEngine.logMailUpdate(oldMail, credentials);
        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public Credentials banUser(long idUser, BanReason reason, long idAdmin) throws NotSuchUserException, UserAlreadyBanException {
        Optional<Credentials> optUser = base.findCredentialsById(idUser);
        Optional<Credentials> optAdmin = base.findCredentialsById(idAdmin);
        if (optUser.isPresent() && optAdmin.isPresent()) {
            var user = optUser.get();

            if (!user.getMail().equals(mailAdmin)) {


                if (user.getBanishment() != null) throw new UserAlreadyBanException();
                var admin = optAdmin.get();
                Banishment be = new Banishment(admin, reason);
                be.setUser(user);
                user.setBanishment(be);

                user = base.saveCredentials(user);
                admin = base.saveCredentials(admin);
                logsEngine.logBan(user, admin, reason);


            }
            return user;
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
            base.deleteBanishment(user.getBanishment());
            user.setBanishment(null);
            user = base.saveCredentials(user);

            logsEngine.logUnban(user, admin);
        } else {
            throw new NotSuchUserException();
        }
    }


}