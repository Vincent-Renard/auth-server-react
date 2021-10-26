package com.example.auth.server.authentification.facade.services.server;

import com.example.auth.server.model.entities.ForbidenDomain;

import java.util.Collection;


public interface DomainService {


	void addForbidenDomain(long idAdmin, String domain);

	void addForbidenDomains(long idAdmin, Collection<String> domains);

	void delForbidenDomain(long idAdmin, String domain);

	Collection<ForbidenDomain> getAllDomainNotAllowed();


}
