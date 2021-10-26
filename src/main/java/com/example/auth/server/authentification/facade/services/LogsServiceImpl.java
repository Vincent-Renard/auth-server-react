package com.example.auth.server.authentification.facade.services;

import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.enums.BanReason;
import com.example.auth.server.model.entities.logs.admin.AdminBanLog;
import com.example.auth.server.model.entities.logs.admin.AdminRoleUpdateLog;
import com.example.auth.server.model.entities.logs.admin.AdminUnbanLog;
import com.example.auth.server.model.entities.logs.user.*;
import com.example.auth.server.model.repositories.CredentialsRepository;
import com.example.auth.server.model.repositories.logs.AdminBanLogRepository;
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


    AdminBanLogRepository adminBanLogRepository;

    @Override
    public void logRoleUpdate(Credentials user, Credentials admin, Collection<String> newRoles) {
        RoleUpdateLog logUser = new RoleUpdateLog(user);

        logUser.setRoles(newRoles);
        logUser.setAdmin(admin);
        user.addLog(logUser);

        users.save(user);

        AdminRoleUpdateLog logAdmin = new AdminRoleUpdateLog(admin, user);
        logAdmin.setRoles(newRoles);
        admin.addLog(logAdmin);

        users.save(admin);
    }


    @Override
    public void logBadPassword(Credentials user) {
        BadPasswordAttempt bpal = new BadPasswordAttempt(user);
        user.addLog(bpal);
        users.save(user);
    }

    @Override
    public void logLogin(Credentials user) {
        SuccessfulLogingLog ull = new SuccessfulLogingLog(user);
        user.addLog(ull);
        users.save(user);

    }

    @Override
    public void logRegistration(Credentials user) {
        user.addLog(new RegistrationLog(user));
        users.save(user);
    }

    @Override
    public void LogRefreshing(Credentials user) {
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
        BanLog bul = new BanLog(user, admin, reason);
        user.addLog(bul);
        user = users.save(user);


        AdminBanLog abul = new AdminBanLog(admin, user, reason);
        admin.addLog(abul);
        users.save(admin);

    }

    @Override
    public void logMailUpdate(String oldMail, Credentials user) {
        MailUpdateLog mul = new MailUpdateLog(user, oldMail);
        user.addLog(mul);
        users.save(user);

    }

    @Override
    public void logUnban(Credentials user, Credentials admin) {
        UnbanLog bul = new UnbanLog(user, admin);
        user.addLog(bul);
        users.save(user);


        AdminUnbanLog abul = new AdminUnbanLog(admin, user);
        admin.addLog(abul);
        users.save(admin);
    }

    @Override
    public void logUnbanDomain(long idAdmin, String domain) {
        //TODO
    }

    @Override
    public void logBanDomain(Credentials admin, String domain) {
        //TODO
    }

    @Override
    public void logAsksServerStateAdmin(Credentials admin) {
        //TODO
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
    public void delogUser(long iduser) {

        //admin role update
        //ADMINban log

        //adminUnban
    }

    @Override
    public void logUpdatePasswordByResetToken(Credentials u) {
        UpdatePasswordByResetToken upbrt = new UpdatePasswordByResetToken(u);
        u.addLog(upbrt);
        users.save(u);

    }

    @Override
    public void askResetPasswordToken(Credentials u) {
        ClaimResetTokenLog claimResetTokenLog = new ClaimResetTokenLog(u);
        u.addLog(claimResetTokenLog);
        //users.save(u);
    }
}
