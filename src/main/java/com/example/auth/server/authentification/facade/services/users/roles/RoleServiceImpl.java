package com.example.auth.server.authentification.facade.services.users.roles;

import com.example.auth.server.authentification.facade.AuthUtils;
import com.example.auth.server.authentification.facade.exceptions.NotSuchAdminException;
import com.example.auth.server.authentification.facade.exceptions.NotSuchUserException;
import com.example.auth.server.authentification.facade.services.LogsService;
import com.example.auth.server.model.PersistenceEngine;
import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.repositories.CredentialsRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RoleServiceImpl implements RoleService, AuthUtils {
	PersistenceEngine base;

	CredentialsRepository userCredentials;

	LogsService logsService;

	@Override
	public Collection<Credentials> getAllUsersWithRole(String role) {
		role = role.toUpperCase();

		return POSSILBES_ROLES.contains(role) ? this.getUsersWithRole(role) : new HashSet<>();
	}

	private Collection<Credentials> getUsersWithRole(String role) {
		return userCredentials.findAll().stream()
				.filter(uc -> uc.getRoles().contains(role))
				.collect(Collectors.toSet());
	}

	@Override
	public Credentials updateRoles(long idUser, Collection<String> newRoles, long idAdmin) throws NotSuchUserException, NotSuchAdminException {
		var currentUserCredentials = base.findCredentialsById(idUser).orElseThrow(NotSuchUserException::new);
		var adminCredentials = base.findCredentialsById(idAdmin).orElseThrow(NotSuchUserException::new);
		newRoles = newRoles.stream().map(String::toUpperCase).collect(Collectors.toSet());


		if (POSSILBES_ROLES.containsAll(newRoles)) {

			if (!newRoles.equals(currentUserCredentials.getRoles())) {
				currentUserCredentials.setRoles(newRoles);
				currentUserCredentials = base.saveCredentials(currentUserCredentials);
				logsService.logRoleUpdate(currentUserCredentials, adminCredentials, newRoles);
			}

			return currentUserCredentials;


		}
		return currentUserCredentials;
	}

}
