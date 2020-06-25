package com.example.auth.server.authentification.facade;

import com.example.auth.server.authentification.facade.persistence.entities.ForbidenDomainEntity;
import com.example.auth.server.authentification.facade.persistence.entities.StoreUser;
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

    ForbidenDomainEntity addForbidenDomain(String domain);

    void delForbidenDomain(String domain);

    Collection<ForbidenDomainEntity> getAllDomainNotAllowed();

    Bearers signIn(String mail, String passsword) throws MailAlreadyTakenException, BadPasswordFormat, InvalidMail, ForbidenDomainMailUse;

    Bearers logIn(String mail, String passsword) throws BadPasswordException, NotSuchUserException;

    Bearers refresh(long iduser) throws NotSuchUserException;

    void clear();

    void signOut(long iduser, String password) throws BadPasswordException, NotSuchUserException;

    StoreUser updateRoles(long iduser, Collection<String> newRoles) throws NotSuchUserException;

    void updatePassword(long iduser, String oldPasssword, String newpasssword) throws NotSuchUserException, BadPasswordException, BadPasswordFormat;

    void updateMail(long iduser, String passsword, String mail) throws MailAlreadyTakenException, NotSuchUserException, InvalidMail, BadPasswordException, ForbidenDomainMailUse;

}
