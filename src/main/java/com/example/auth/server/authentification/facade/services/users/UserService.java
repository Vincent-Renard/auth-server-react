package com.example.auth.server.authentification.facade.services.users;

import com.example.auth.server.authentification.facade.exceptions.*;
import com.example.auth.server.model.entities.Credentials;

public interface UserService {


	void signOut(long iduser) throws NotSuchUserException, UserBanException;


	Credentials showUser(long iduser) throws NotSuchUserException;


	void updateMail(long iduser, String mail) throws MailAlreadyTakenException, NotSuchUserException, InvalidMailException, ForbiddenDomainMailUseException, UserBanException;

	boolean userExistsWithMail(String mail);
}
