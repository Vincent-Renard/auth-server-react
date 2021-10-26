package com.example.auth.server.authentification.facade.services.users.roles;

import com.example.auth.server.authentification.facade.exceptions.NotSuchAdminException;
import com.example.auth.server.authentification.facade.exceptions.NotSuchUserException;
import com.example.auth.server.model.entities.Credentials;

import java.util.Collection;

public interface RoleService {
	Credentials updateRoles(long iduser, Collection<String> newRoles, long idAdmin) throws NotSuchUserException, NotSuchAdminException;

	Collection<Credentials> getAllUsersWithRole(String role);
}
