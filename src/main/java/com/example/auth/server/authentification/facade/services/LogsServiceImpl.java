package com.example.auth.server.authentification.facade.services;

import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.enums.BanReason;
import com.example.auth.server.model.entities.logs.admin.AdminBanLog;
import com.example.auth.server.model.entities.logs.admin.AdminRoleUpdateLog;
import com.example.auth.server.model.entities.logs.admin.AdminUnbanLog;
import com.example.auth.server.model.entities.logs.user.*;
import com.example.auth.server.model.repositories.CredentialsRepository;
import com.example.auth.server.model.repositories.logs.BanLogRepository;
import com.example.auth.server.model.repositories.logs.RoleUpdateLogRepository;
import com.example.auth.server.model.repositories.logs.UnBanLogRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @autor Vincent
 * @date 02/09/2020
 */
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogsServiceImpl implements LogsService {


	CredentialsRepository users;


	RoleUpdateLogRepository roleUpdateLogRepository;


	BanLogRepository banLogs;


	UnBanLogRepository unbanLogs;


	@Override
	public void logRoleUpdate(Credentials user, Credentials admin, Collection<String> newRoles) {
		var logUser = new RoleUpdateLog(user);

		logUser.setRoles(newRoles);
		logUser.setAdmin(admin);
		user.addLog(logUser);

		users.save(user);

		var logAdmin = new AdminRoleUpdateLog(admin, user);
		logAdmin.setRoles(newRoles);
		admin.addLog(logAdmin);

		users.save(admin);
	}


	@Override
	public void logBadPassword(Credentials user) {
		var bpal = new BadPasswordAttempt(user);
		user.addLog(bpal);
		users.save(user);
	}

	@Override
	public void logLogin(Credentials user) {
		var ull = new SuccessfulLogingLog(user);
		user.addLog(ull);
		users.save(user);

	}

	@Override
	public void logRegistration(Credentials user) {
		user.addLog(new RegistrationLog(user));
		users.save(user);
	}

	@Override
	public void logRefreshing(Credentials user) {
		user.addLog(new RefreshingTokensLog(user));
		users.save(user);
	}

	@Override
	public void logUpdatePassword(Credentials user) {
		user.addLog(new PasswordUpdateLog(user));
		users.save(user);
	}

	@Override
	public void logBan(Credentials user, Credentials admin, BanReason reason) {
		var bul = new BanLog(user, admin, reason);
		user.addLog(bul);
		user = users.save(user);


		var abul = new AdminBanLog(admin, user, reason);
		admin.addLog(abul);
		users.save(admin);

	}

	@Override
	public void logMailUpdate(String oldMail, Credentials user) {
		var mul = new MailUpdateLog(user, oldMail);
		user.addLog(mul);
		users.save(user);

	}

	@Override
	public void logUnban(Credentials user, Credentials admin) {
		var bul = new UnbanLog(user, admin);
		user.addLog(bul);
		users.save(user);


		var abul = new AdminUnbanLog(admin, user);
		admin.addLog(abul);
		users.save(admin);
	}


	@Override
	public void delogAdmin(long iduser) {
		var roleUpByAdmin = roleUpdateLogRepository.findAllByAdmin_IdUser(iduser);
		roleUpByAdmin.forEach(e -> e.setAdmin(null));
		roleUpdateLogRepository.saveAll(roleUpByAdmin);

		var banLogsByAdmin = banLogs.findAllByAdmin_IdUser(iduser);
		banLogsByAdmin.forEach(banLog -> banLog.setAdmin(null));
		banLogs.saveAll(banLogsByAdmin);

		var unbanLogsByAdmin = unbanLogs.findAllByAdmin_IdUser(iduser);
		unbanLogsByAdmin.forEach(unbanLog -> unbanLog.setAdmin(null));
		unbanLogs.saveAll(unbanLogsByAdmin);


	}

	@Override
	public void logUpdatePasswordByResetToken(Credentials u) {
		var upbrt = new UpdatePasswordByResetToken(u);
		u.addLog(upbrt);
		users.save(u);
	}

	@Override
	public void askResetPasswordToken(Credentials u) {
		var claimResetTokenLog = new ClaimResetTokenLog(u);
		u.addLog(claimResetTokenLog);
		users.save(u);
	}
}
