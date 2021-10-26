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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AdminServiceImpl implements AdminService, AuthUtils {


	LogsService logsService;

	PersistenceEngine base;

	UserService userService;

	BanishmentRepository bans;


	ForbidenDomainRepository forbidenDomains;
	JwtEncoder tokenEncoder;

	CredentialsRepository credentialsRepo;

	@Override
	public AuthServerStateAdmin getServerStateAdmin(long idAdmin) throws NotSuchUserException {

		Optional<Credentials> optAdmin = base.findCredentialsById(idAdmin);
		if (optAdmin.isPresent()) {
			AuthServerStateAdmin p = new AuthServerStateAdmin();

			p.setAuthTokenTTL(tokenEncoder.getAuthTTL());
			p.setRefreshTokenTTL(tokenEncoder.getRefreshTTL());
			//p.setStartDateServer(startDate);

			p.setForbidenDomains(base.findAllDomains().stream().map(ForbidenDomain::getDomain).collect(Collectors.toSet()));
			p.setKey(tokenEncoder.getPublicKey().getEncoded());

			p.setNbUsersRegistered(credentialsRepo.count());
			p.setNbUsersAdminsRegistered(credentialsRepo.findAll().stream().filter(c -> c.getRoles().contains("ADMIN")).count());
			var admin = optAdmin.get();
			logsService.logAsksServerStateAdmin(admin);
			return p;
		} else throw new NotSuchUserException();

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
		Optional<Credentials> optCredentials = credentialsRepo.findById(idAdmin);
		if (optCredentials.isPresent()) {
			return userService.showUser(idUser);
		} else {
			throw new NotSuchUserException();
		}

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
		Optional<Credentials> optUser = base.findCredentialsById(idUser);
		Optional<Credentials> optAdmin = base.findCredentialsById(idAdmin);
		if (optUser.isPresent() && optAdmin.isPresent()) {

			var user = optUser.get();
			var admin = optAdmin.get();
			this.deleteBanishment(user.getBanishment());
			user.setBanishment(null);
			user = base.saveCredentials(user);

			logsService.logUnban(user, admin);
		} else {
			throw new NotSuchUserException();
		}
	}


	@Override
	public Credentials banUser(long idUser, BanReason reason, long idAdmin) throws NotSuchUserException, UserAlreadyBanException {
		Optional<Credentials> optUser = base.findCredentialsById(idUser);
		Optional<Credentials> optAdmin = base.findCredentialsById(idAdmin);
		if (optUser.isPresent() && optAdmin.isPresent()) {
			var user = optUser.get();

			if (!user.getMail().equals(mailAdmin)) {


				if (user.getBanishment() != null) throw new UserAlreadyBanException();
				var admin = optAdmin.get();
				Banishment be = new Banishment(admin, reason);
				be.setUser(user);
				user.setBanishment(be);

				user = base.saveCredentials(user);
				admin = base.saveCredentials(admin);
				logsService.logBan(user, admin, reason);


			}
			return user;
		} else {
			throw new NotSuchUserException();
		}
	}

	@Override
	public Collection<Credentials> getAllUsersWithDomainsNotAllowed() {
		Set<String> fds = forbidenDomains.findAll().stream().map(ForbidenDomain::getDomain).collect(Collectors.toSet());
		return credentialsRepo.findAll()
				.stream()
				.filter(uc -> fds.contains(uc.getMail().split("@")[1]))
				.collect(Collectors.toSet());
	}


}
