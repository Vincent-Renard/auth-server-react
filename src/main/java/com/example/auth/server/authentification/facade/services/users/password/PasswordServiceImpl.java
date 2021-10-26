package com.example.auth.server.authentification.facade.services.users.password;

import com.example.auth.server.authentification.facade.AuthUtils;
import com.example.auth.server.authentification.facade.exceptions.BadPasswordFormatException;
import com.example.auth.server.authentification.facade.exceptions.NotSuchUserException;
import com.example.auth.server.authentification.facade.exceptions.TokenNotFoundException;
import com.example.auth.server.authentification.facade.exceptions.UserBanException;
import com.example.auth.server.authentification.facade.services.LogsService;
import com.example.auth.server.authentification.facade.services.server.ResetTokenService;
import com.example.auth.server.model.PersistenceEngine;
import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.ResetPasswordToken;
import com.example.auth.server.model.repositories.ResetPasswordTokenRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PasswordServiceImpl implements PasswordService, AuthUtils {

	LogsService logsService;

	PersistenceEngine base;

	ResetPasswordTokenRepository resetPasswordTokens;

	ResetTokenService resetTokenService;
	PasswordEncoder passwordEncoder;

	@Override
	public String encodePassword(String clearPassword) {
		return passwordEncoder.encode(clearPassword);
	}

	@Override
	public boolean passwordMatches(String clearPassword, String encodedPassword) {
		return passwordEncoder.matches(clearPassword, encodedPassword);
	}


	@Override
	public ResetPasswordToken askResetPasswordToken(String mail) throws NotSuchUserException, UserBanException {
		var optUser = base.findCredentialsByMail(mail);
		if (optUser.isPresent()) {
			var u = optUser.get();
			if (u.getBanishment() != null)
				throw new UserBanException();

			logsService.askResetPasswordToken(u);
			return resetTokenService.generateResetToken(u);
		} else throw new NotSuchUserException();

	}

	@Override
	public void useResetPasswordToken(String key, String newPassword) throws UserBanException, BadPasswordFormatException, TokenNotFoundException {
		var optToken = resetPasswordTokens.findById(key);
		if (optToken.isPresent()) {
			var token = optToken.get();
			var u = token.getUser();
			if (u.getBanishment() != null)
				throw new UserBanException();
			if (!passwordChecker.test(newPassword)) throw new BadPasswordFormatException();

			u.setPassword(this.encodePassword(newPassword));


			this.useToken(token);
			base.saveCredentials(u);
			logsService.logUpdatePasswordByResetToken(u);
			base.cleanOldAndUnusedResetTokens(TTL_SECONDS_RESET_PASSWORD_TOKEN);
		} else throw new TokenNotFoundException();
	}

	private void useToken(ResetPasswordToken token) {
		token.use();
		resetPasswordTokens.save(token);
	}

	@Override
	public void updatePassword(long idUser, String newpasssword) throws NotSuchUserException, BadPasswordFormatException, UserBanException {
		Optional<Credentials> optCredentials = base.findCredentialsById(idUser);

		if (optCredentials.isPresent()) {
			var credentials = optCredentials.get();
			if (credentials.getBanishment() != null)
				throw new UserBanException();
			if (!passwordChecker.test(newpasssword)) throw new BadPasswordFormatException();

			credentials.setPassword(this.encodePassword(newpasssword));

			base.saveCredentials(credentials);
			logsService.logUpdatePassword(credentials);
		} else {
			throw new NotSuchUserException();
		}
	}
}
