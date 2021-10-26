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
import java.util.Optional;
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

		if (POSSILBES_ROLES.contains(role)) {
			return this.getUsersWithRole(role);
		}

		return new HashSet<>();
	}

	private Collection<Credentials> getUsersWithRole(String role) {
		return userCredentials.findAll().stream()
				.filter(uc -> uc.getRoles().contains(role))
				.collect(Collectors.toSet());
	}

	@Override
	public Credentials updateRoles(long idUser, Collection<String> newRoles, long idAdmin) throws NotSuchUserException, NotSuchAdminException {
		Optional<Credentials> optCredentialsUser = base.findCredentialsById(idUser);
		Optional<Credentials> optCredentialsAdmin = base.findCredentialsById(idAdmin);
		newRoles = newRoles.stream().map(String::toUpperCase).collect(Collectors.toSet());

		if (!optCredentialsUser.isPresent()) {
			throw new NotSuchUserException();
		}
		if (!optCredentialsAdmin.isPresent()) {
			throw new NotSuchAdminException();
		}
		var userCredentials = optCredentialsUser.get();
		if (POSSILBES_ROLES.containsAll(newRoles)) {

			var adminCredentials = optCredentialsAdmin.get();
			if (!newRoles.equals(userCredentials.getRoles())) {
				userCredentials.setRoles(newRoles);
				userCredentials = base.saveCredentials(userCredentials);
				logsService.logRoleUpdate(userCredentials, adminCredentials, newRoles);
			}

			return userCredentials;


		}
		return userCredentials;
	}

}
