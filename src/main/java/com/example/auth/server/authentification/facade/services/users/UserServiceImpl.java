package com.example.auth.server.authentification.facade.services.users;

import com.example.auth.server.authentification.facade.AuthUtils;
import com.example.auth.server.authentification.facade.exceptions.*;
import com.example.auth.server.authentification.facade.services.LogsService;
import com.example.auth.server.model.PersistenceEngine;
import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.ResetPasswordToken;
import com.example.auth.server.model.repositories.BanishmentRepository;
import com.example.auth.server.model.repositories.CredentialsRepository;
import com.example.auth.server.model.repositories.ResetPasswordTokenRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserServiceImpl implements UserService, AuthUtils {

	LogsService logsService;
	BanishmentRepository banishmentRepository;
	PersistenceEngine base;
	CredentialsRepository credentialsRepository;

	ResetPasswordTokenRepository resetPasswordTokenRepository;

	private void deleteCredentialsById(long iduser) {
		var opt = credentialsRepository.findById(iduser);
		if (opt.isPresent()) {
			var cred = opt.get();
			if (cred.getRoles().contains("ADMIN")) {
				var listBansByAdmin = banishmentRepository.findBanishmentByAdmin_IdUser(iduser);
				listBansByAdmin.forEach(banishment -> banishment.setAdmin(null));
				banishmentRepository.saveAll(listBansByAdmin);
				logsService.delogAdmin(iduser);


			}

			var ltokens = resetPasswordTokenRepository.findAllByUserIdUser(iduser);
			ltokens.forEach(t -> t.setUser(null));
			resetPasswordTokenRepository.saveAll(ltokens);
			var ids = ltokens.stream().map(ResetPasswordToken::getResetToken);
			ids.forEach(id -> resetPasswordTokenRepository.deleteById(id));

			credentialsRepository.deleteById(iduser);

		}

	}

	@Override
	public void signOut(long idUser) throws NotSuchUserException, UserBanException {
		var credentials = showUser(idUser);
		if (credentials.getBanishment() != null)
			throw new UserBanException();

		this.deleteCredentialsById(idUser);

	}

	@Override
	public boolean userExistsWithMail(String mail) {
		mail = mail.toLowerCase();
		return credentialsRepository.existsByMail(mail);
	}

	@Override
	public void updateMail(long idUser, String newmail) throws MailAlreadyTakenException, NotSuchUserException, InvalidMailException, ForbiddenDomainMailUseException, UserBanException {
		var credentials = showUser(idUser);

		if (credentials.getBanishment() != null)
			throw new UserBanException();

		if (!mailChecker.test(newmail))
			throw new InvalidMailException();
		String domain = newmail.split("@")[1];
		if (base.existDomainByName(domain))
			throw new ForbiddenDomainMailUseException();

		if (userExistsWithMail(newmail))
			throw new MailAlreadyTakenException();

		if (credentials.getMail().equals(MAIL_ADMIN))
			newmail = MAIL_ADMIN;

		String oldMail = credentials.getMail();
		credentials.setMail(newmail);
		base.saveCredentials(credentials);
		logsService.logMailUpdate(oldMail, credentials);

	}

	@Override
	public Credentials showUser(long idUser) throws NotSuchUserException {
		return base.findCredentialsById(idUser).orElseThrow(NotSuchUserException::new);
	}

}
