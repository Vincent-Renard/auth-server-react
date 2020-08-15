package com.example.auth.server.authentification.facade;

import com.example.auth.server.authentification.facade.persistence.entities.BanReason;
import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.ForbidenDomain;
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

    AuthServerStateAdmin getServerStateAdmin();

    void addForbidenDomain(String domain);

    void addForbidenDomains(Collection<String> domains);

    void delForbidenDomain(String domain);

    Collection<ForbidenDomain> getAllDomainNotAllowed();

    Collection<Credentials> getAllUsersWithDomainsNotAllowed();

    Collection<Credentials> getAllUsersWithDomain(String domain);

    UserToken signIn(String mail, String passsword) throws MailAlreadyTakenException, BadPasswordFormat, InvalidMail, ForbidenDomainMailUse, UserBan;

    Bearers logIn(String mail, String passsword) throws BadPasswordException, NotSuchUserException, UserBan;

    Bearers refresh(String token) throws NotSuchUserException, UserBan, NoToken, InvalidToken, TokenExpired;

    void clear();

    void signOut(long iduser) throws NotSuchUserException, UserBan;

    Credentials updateRoles(long iduser, Collection<String> newRoles) throws NotSuchUserException;

    Credentials showUser(long iduser) throws NotSuchUserException;

    void updatePassword(long iduser, String newpasssword) throws NotSuchUserException, BadPasswordFormat, UserBan;

    void updateMail(long iduser, String mail) throws MailAlreadyTakenException, NotSuchUserException, InvalidMail, ForbidenDomainMailUse, UserBan;

    Credentials banUser(long idUser, BanReason reason, long idAdmin) throws NotSuchUserException, UserAlreadyBanException;

    void unBanUser(long idUser, long idAdmin) throws NotSuchUserException;

    Collection<Credentials> getAllUsersWithRole(String role);
}
