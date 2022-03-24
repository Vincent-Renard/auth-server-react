package com.example.auth.server.authentification.facade.services.admin;

import com.example.auth.server.authentification.facade.AuthUtils;
import com.example.auth.server.authentification.facade.dtos.out.AuthServerStateAdmin;
import com.example.auth.server.authentification.facade.exceptions.NotSuchUserException;
import com.example.auth.server.authentification.facade.exceptions.UserAlreadyBanException;
import com.example.auth.server.authentification.facade.services.LogsService;
import com.example.auth.server.authentification.facade.services.users.UserService;
import com.example.auth.server.authentification.token.manager.JwtEncoder;
import com.example.auth.server.model.PersistenceEngine;
import com.example.auth.server.model.entities.Banishment;
import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.ForbidenDomain;
import com.example.auth.server.model.entities.enums.BanReason;
import com.example.auth.server.model.repositories.BanishmentRepository;
import com.example.auth.server.model.repositories.CredentialsRepository;
import com.example.auth.server.model.repositories.ForbidenDomainRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AdminServiceImpl implements AuthUtils, AdminService {


	LogsService logsService;

	PersistenceEngine base;

	UserService userService;

	BanishmentRepository bans;


	ForbidenDomainRepository forbidenDomains;
	JwtEncoder tokenEncoder;

	CredentialsRepository credentialsRepo;

	@Override
	public AuthServerStateAdmin getServerStateAdmin(long idAdmin) throws NotSuchUserException {

		var admin = base.findCredentialsById(idAdmin).orElseThrow(NotSuchUserException::new);
		var p = new AuthServerStateAdmin();

		p.setAuthTokenTTL(tokenEncoder.getAuthTTL());
		p.setRefreshTokenTTL(tokenEncoder.getRefreshTTL());
		//p.setStartDateServer(startDate);

		p.setForbidenDomains(base.findAllDomains().stream().map(ForbidenDomain::getDomain).collect(Collectors.toSet()));
		p.setKey(tokenEncoder.getPublicKey().getEncoded());

		p.setNbUsersRegistered(credentialsRepo.count());
		p.setNbUsersAdminsRegistered(credentialsRepo.findAll().stream().filter(c -> c.getRoles().contains("ADMIN")).count());
		return p;

	}


	@Override
	public Collection<Credentials> getAllUsersWithDomain(String domain) {
		var domainToSearch = domain.toLowerCase();
		return credentialsRepo.findAll()
				.stream()
				.filter(credential -> credential.getMail().split("@")[1].equals(domainToSearch))
				.collect(Collectors.toSet());
	}


	@Override
	public Credentials showUser(long idAdmin, long idUser) throws NotSuchUserException {
		if (!credentialsRepo.existsById(idAdmin)) throw new NotSuchUserException();
		return userService.showUser(idUser);
	}


	public void deleteBanishment(Banishment banishment) {
		var user = banishment.getUser();
		banishment.setUser(null);
		banishment.setAdmin(null);
		bans.save(banishment);
		user.setBanishment(null);
		credentialsRepo.save(user);

	}

	@Override
	public void unBanUser(long idUser, long idAdmin) throws NotSuchUserException {
		var user = base.findCredentialsById(idUser).orElseThrow(NotSuchUserException::new);
		var admin = base.findCredentialsById(idAdmin).orElseThrow(NotSuchUserException::new);
		this.deleteBanishment(user.getBanishment());
		user.setBanishment(null);
		user = base.saveCredentials(user);

		logsService.logUnban(user, admin);
	}


	@Override
	public Credentials banUser(long idUser, BanReason reason, long idAdmin) throws NotSuchUserException, UserAlreadyBanException {
		var user = base.findCredentialsById(idUser).orElseThrow(NotSuchUserException::new);
		var admin = base.findCredentialsById(idAdmin).orElseThrow(NotSuchUserException::new);

		if (!user.getMail().equals(MAIL_ADMIN)) {
			if (user.getBanishment() != null) throw new UserAlreadyBanException();

			var banishment = new Banishment(admin, reason);
			banishment.setUser(user);
			user.setBanishment(banishment);

			user = base.saveCredentials(user);
			admin = base.saveCredentials(admin);
			logsService.logBan(user, admin, reason);

		}
		return user;
	}

	@Override
	public Collection<Credentials> getAllUsersWithDomainsNotAllowed() {
		var forbidenDomainsSet = forbidenDomains.findAll().stream().map(ForbidenDomain::getDomain).collect(Collectors.toSet());
		return credentialsRepo.findAll()
				.stream()
				.filter(uc -> forbidenDomainsSet.contains(uc.getMail().split("@")[1]))
				.collect(Collectors.toSet());
	}


}
