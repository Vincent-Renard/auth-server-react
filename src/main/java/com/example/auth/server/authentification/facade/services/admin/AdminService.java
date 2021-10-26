package com.example.auth.server.authentification.facade.services.admin;

import com.example.auth.server.authentification.facade.dtos.out.AuthServerStateAdmin;
import com.example.auth.server.authentification.facade.exceptions.NotSuchUserException;
import com.example.auth.server.authentification.facade.exceptions.UserAlreadyBanException;
import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.enums.BanReason;

import java.util.Collection;

public interface AdminService {


	AuthServerStateAdmin getServerStateAdmin(long idAdmin) throws NotSuchUserException;

	Credentials banUser(long idUser, BanReason reason, long idAdmin) throws NotSuchUserException, UserAlreadyBanException;

	void unBanUser(long idUser, long idAdmin) throws NotSuchUserException;

	Credentials showUser(long idAdmin, long iduser) throws NotSuchUserException;

	Collection<Credentials> getAllUsersWithDomainsNotAllowed();

	Collection<Credentials> getAllUsersWithDomain(String domain);
}
