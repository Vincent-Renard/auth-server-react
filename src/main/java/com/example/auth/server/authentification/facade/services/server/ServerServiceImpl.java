package com.example.auth.server.authentification.facade.services.server;

import com.example.auth.server.authentification.facade.AuthUtils;
import com.example.auth.server.authentification.facade.dtos.out.AuthServerStatePublic;
import com.example.auth.server.authentification.facade.services.users.UserService;
import com.example.auth.server.authentification.facade.services.users.password.PasswordService;
import com.example.auth.server.authentification.token.manager.JwtEncoder;
import com.example.auth.server.model.PersistenceEngine;
import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.repositories.CredentialsRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ServerServiceImpl implements ServerService, AuthUtils {

	static final Logger logger = LoggerFactory.getLogger(ServerServiceImpl.class);


	final PersistenceEngine base;


	final JwtEncoder tokenEncoder;

	final PasswordService passwordService;

	final CredentialsRepository credentials;

	final UserService userService;
	LocalDateTime startDate;

	@PostConstruct
	private void init() {
		startDate = LocalDateTime.now();
		fill();

	}

	@Override
	public void clear() {
		credentials.deleteAll();
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
		for(int i = 0; i <= lenght; i++) {
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

		if (!userService.userExistsWithMail(mailAdmin)) {

			var admin = new Credentials(mailAdmin, passwordService.encodePassword(password), Set.of("USER", "ADMIN"));
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

}
