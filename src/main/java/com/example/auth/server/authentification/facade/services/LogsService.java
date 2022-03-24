package com.example.auth.server.authentification.facade.services;

import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.enums.BanReason;

import java.util.Collection;

public interface LogsService {
	void logRoleUpdate(Credentials user, Credentials admin, Collection<String> newRoles);

	void logBadPassword(Credentials user);

	void logLogin(Credentials user);

	void logRegistration(Credentials user);

	void logRefreshing(Credentials user);

	void logUpdatePassword(Credentials user);

	void logBan(Credentials user, Credentials admin, BanReason reason);

	void logMailUpdate(String oldMail, Credentials user);

	void logUnban(Credentials user, Credentials admin);

	void delogAdmin(long iduser);

	void logUpdatePasswordByResetToken(Credentials u);

	void askResetPasswordToken(Credentials u);
}
