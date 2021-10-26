package com.example.auth.server.authentification.facade.services.server;

import com.example.auth.server.authentification.facade.dtos.out.Bearers;
import com.example.auth.server.authentification.facade.dtos.out.UserToken;
import com.example.auth.server.authentification.facade.exceptions.*;

public interface TokenService {

	Bearers logIn(String mail, String passsword) throws BadPasswordException, NotSuchUserException, UserBanException;

	Bearers refresh(String token) throws NotSuchUserException, UserBanException, NoTokenException, InvalidTokenException, TokenExpiredException;

	UserToken signIn(String mail, String passsword) throws MailAlreadyTakenException, BadPasswordFormatException, InvalidMailException, ForbiddenDomainMailUseException, UserBanException;


}
