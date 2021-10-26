package com.example.auth.server.authentification.facade.services.server;

import com.example.auth.server.authentification.facade.AuthUtils;
import com.example.auth.server.authentification.facade.dtos.out.Bearers;
import com.example.auth.server.authentification.facade.dtos.out.UserToken;
import com.example.auth.server.authentification.facade.exceptions.*;
import com.example.auth.server.authentification.facade.services.LogsService;
import com.example.auth.server.authentification.facade.services.users.password.PasswordService;
import com.example.auth.server.authentification.token.manager.JwtDecoder;
import com.example.auth.server.authentification.token.manager.JwtEncoder;
import com.example.auth.server.model.PersistenceEngine;
import com.example.auth.server.model.entities.Credentials;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TokenServiceImpl implements TokenService, AuthUtils {
	LogsService logsService;

	PersistenceEngine base;

	JwtDecoder tokenDecoder;
	JwtEncoder tokenEncoder;

	PasswordService passwordService;
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

		var credentials = new Credentials(mailUser, passwordService.encodePassword(passsword), rolesOfUser);


		var user = base.saveCredentials(credentials);
		logsService.logRegistration(user);
		Bearers tokens = tokenEncoder.genBoth(user.getIdUser(), user.getRoles());
		return new UserToken(user.getIdUser(), tokens);
	}

	@Override
	public Bearers logIn(String mail, String password) throws BadPasswordException, NotSuchUserException, UserBanException {
		Optional<Credentials> optCredentials = base.findCredentialsByMail(mail);
		if (optCredentials.isPresent()) {
			var credentials = optCredentials.get();


			if (!passwordService.passwordMatches(password, credentials.getPassword())) {
				logsService.logBadPassword(credentials);
				throw new BadPasswordException();
			}
			if (credentials.getBanishment() != null) {
				throw new UserBanException();
			}

			logsService.logLogin(credentials);
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

			logsService.LogRefreshing(credentials);
			return tokenEncoder.genBoth(idUser, credentials.getRoles());

		} else {
			throw new NotSuchUserException();
		}

	}
}
