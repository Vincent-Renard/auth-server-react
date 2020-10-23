package com.example.auth.server.authentification.facade;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.ForbidenDomain;
import com.example.auth.server.authentification.facade.persistence.entities.ResetPasswordToken;
import com.example.auth.server.authentification.facade.persistence.entities.enums.BanReason;
import com.example.auth.server.authentification.facade.pojos.UserToken;
import com.example.auth.server.model.dtos.out.AuthServerStateAdmin;
import com.example.auth.server.model.dtos.out.AuthServerStatePublic;
import com.example.auth.server.model.dtos.out.Bearers;
import com.example.auth.server.model.exceptions.*;

import java.security.PublicKey;
import java.util.Collection;

/**
 * @autor Vincent
 * @date 11/03/2020
 */

public interface AuthService {

    PublicKey publicKey();

    AuthServerStatePublic getServerStatePublic();

    AuthServerStateAdmin getServerStateAdmin(long idAdmin) throws NotSuchUserException;

    void addForbidenDomain(long idAdmin, String domain);

    void addForbidenDomains(long idAdmin, Collection<String> domains);

    void delForbidenDomain(long idAdmin, String domain);

    Collection<ForbidenDomain> getAllDomainNotAllowed();

    Collection<Credentials> getAllUsersWithDomainsNotAllowed();

    Collection<Credentials> getAllUsersWithDomain(String domain);

    UserToken signIn(String mail, String passsword) throws MailAlreadyTakenException, BadPasswordFormatException, InvalidMailException, ForbiddenDomainMailUseException, UserBanException;

    Bearers logIn(String mail, String passsword) throws BadPasswordException, NotSuchUserException, UserBanException;

    Bearers refresh(String token) throws NotSuchUserException, UserBanException, NoTokenException, InvalidTokenException, TokenExpiredException;

    void clear();

    void signOut(long iduser) throws NotSuchUserException, UserBanException;

    Credentials updateRoles(long iduser, Collection<String> newRoles, long idAdmin) throws NotSuchUserException, NotSuchAdminException;

    Credentials showUser(long iduser) throws NotSuchUserException;

    Credentials showUser(long idAdmin, long iduser) throws NotSuchUserException;

    void updatePassword(long iduser, String newpasssword) throws NotSuchUserException, BadPasswordFormatException, UserBanException;

    void updateMail(long iduser, String mail) throws MailAlreadyTakenException, NotSuchUserException, InvalidMailException, ForbiddenDomainMailUseException, UserBanException;

    Credentials banUser(long idUser, BanReason reason, long idAdmin) throws NotSuchUserException, UserAlreadyBanException;

    void unBanUser(long idUser, long idAdmin) throws NotSuchUserException;

    Collection<Credentials> getAllUsersWithRole(String role);

    ResetPasswordToken askResetPasswordToken(String mail) throws NotSuchUserException, UserBanException;

    void useResetPasswordToken(String key, String newPassword) throws UserBanException, BadPasswordFormatException, TokenNotFoundException;
}
