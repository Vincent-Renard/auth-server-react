package com.example.auth.server.authentification.facade.services.users.password;

import com.example.auth.server.authentification.facade.exceptions.BadPasswordFormatException;
import com.example.auth.server.authentification.facade.exceptions.NotSuchUserException;
import com.example.auth.server.authentification.facade.exceptions.TokenNotFoundException;
import com.example.auth.server.authentification.facade.exceptions.UserBanException;
import com.example.auth.server.model.entities.ResetPasswordToken;

public interface PasswordService {


	String encodePassword(String clearPassword);

	boolean passwordMatches(String clearPassword, String encodedPassword);

	ResetPasswordToken askResetPasswordToken(String mail) throws NotSuchUserException, UserBanException;

	void useResetPasswordToken(String key, String newPassword) throws UserBanException, BadPasswordFormatException, TokenNotFoundException;

	void updatePassword(long iduser, String newpasssword) throws NotSuchUserException, BadPasswordFormatException, UserBanException;

}
