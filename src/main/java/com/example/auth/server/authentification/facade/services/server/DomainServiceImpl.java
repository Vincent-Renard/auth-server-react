package com.example.auth.server.authentification.facade.services.server;

import com.example.auth.server.authentification.facade.services.LogsService;
import com.example.auth.server.model.PersistenceEngine;
import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.ForbidenDomain;
import com.example.auth.server.model.repositories.ForbidenDomainRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DomainServiceImpl implements DomainService {

	LogsService logsService;

	PersistenceEngine base;
	ForbidenDomainRepository domainRepository;

	@Override
	public void addForbidenDomain(long idAdmin, String domain) {
		var optAdmin = base.findCredentialsById(idAdmin);
		if (optAdmin.isPresent()) {
			var admin = optAdmin.get();
			domain = domain.toLowerCase();
			domainRepository.save(new ForbidenDomain(admin, domain));
			logsService.logBanDomain(admin, domain);
		}

	}

	public void saveDomains(Credentials admin, Collection<String> domains) {

		Set<String> deja = domainRepository.findAll()
				.stream()
				.map(ForbidenDomain::getDomain)
				.collect(Collectors.toSet());

		Set<ForbidenDomain> ds = domains
				.stream()
				.map(String::toLowerCase)
				.filter(d -> !deja.contains(d))
				.map(d -> new ForbidenDomain(admin, d))
				.collect(Collectors.toSet());
		ds.forEach(forbidenDomain -> logsService.logBanDomain(forbidenDomain.getAdmin(), forbidenDomain.getDomain()));
		domainRepository.saveAll(ds);
	}


	@Override
	public void addForbidenDomains(long idAdmin, Collection<String> domains) {
		Optional<Credentials> optAdmin = base.findCredentialsById(idAdmin);
		optAdmin.ifPresent(credentials -> this.saveDomains(credentials, domains));


	}

	@Override
	public void delForbidenDomain(long idAdmin, String domain) {
		domainRepository.deleteById(domain.toLowerCase(Locale.ROOT));

		logsService.logUnbanDomain(idAdmin, domain);

	}

	@Override
	public Collection<ForbidenDomain> getAllDomainNotAllowed() {

		return base.findAllDomains();
	}


}
