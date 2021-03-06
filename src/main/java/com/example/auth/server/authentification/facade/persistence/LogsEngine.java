package com.example.auth.server.authentification.facade.persistence;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.enums.BanReason;
import com.example.auth.server.authentification.facade.persistence.entities.logs.admin.AdminBanLog;
import com.example.auth.server.authentification.facade.persistence.entities.logs.admin.AdminRoleUpdateLog;
import com.example.auth.server.authentification.facade.persistence.entities.logs.admin.AdminUnbanLog;
import com.example.auth.server.authentification.facade.persistence.entities.logs.user.*;
import com.example.auth.server.authentification.facade.persistence.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @autor Vincent
 * @date 02/09/2020
 */
@Component
public class LogsEngine {

    @Autowired
    CredentialsRepository users;

    @Autowired
    RoleUpdateLogRepository roleUpdateLogRepository;

    @Autowired
    BanLogRepository banLogs;

    @Autowired
    UnBanLogRepository unbanLogs;

    @Autowired
    AdminBanLogRepository adminBanLogRepository;

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


    public void logBadPassword(Credentials user) {
        BadPasswordAttempt bpal = new BadPasswordAttempt(user);
        user.addLog(bpal);
        users.save(user);
    }

    public void logLogin(Credentials user) {
        SuccessfulLogingLog ull = new SuccessfulLogingLog(user);
        user.addLog(ull);
        users.save(user);

    }

    public void logRegistration(Credentials user) {
        user.addLog(new RegistrationLog(user));
        users.save(user);
    }

    public void LogRefreshing(Credentials user) {
        user.addLog(new RefreshingTokensLog(user));
        users.save(user);
    }

    public void logUpdatePassword(Credentials user) {
        user.addLog(new PasswordUpdateLog(user));
        users.save(user);
    }

    public void logBan(Credentials user, Credentials admin, BanReason reason) {
        BanLog bul = new BanLog(user, admin, reason);
        user.addLog(bul);
        user = users.save(user);


        AdminBanLog abul = new AdminBanLog(admin, user, reason);
        admin.addLog(abul);
        users.save(admin);

    }

    public void logMailUpdate(String oldMail, Credentials user) {
        MailUpdateLog mul = new MailUpdateLog(user, oldMail);
        user.addLog(mul);
        users.save(user);

    }

    public void logUnban(Credentials user, Credentials admin) {
        UnbanLog bul = new UnbanLog(user, admin);
        user.addLog(bul);
        users.save(user);


        AdminUnbanLog abul = new AdminUnbanLog(admin, user);
        admin.addLog(abul);
        users.save(admin);
    }

    public void logUnbanDomain(long idAdmin, String domain) {
        //TODO
    }

    public void logBanDomain(Credentials admin, String domain) {
        //TODO
    }

    public void logAsksServerStateAdmin(Credentials admin) {
        //TODO
    }

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

    public void delogUser(long iduser) {

        //admin role update
        //ADMINban log

        //adminUnban
    }

    public void logUpdatePasswordByResetToken(Credentials u) {
        UpdatePasswordByResetToken upbrt = new UpdatePasswordByResetToken(u);
        u.addLog(upbrt);
        users.save(u);

    }

    public void askResetPasswordToken(Credentials u) {
        ClaimResetTokenLog claimResetTokenLog = new ClaimResetTokenLog(u);
        u.addLog(claimResetTokenLog);
        System.err.println("u: "+u.getIdUser()+" "+claimResetTokenLog.toString());
        //users.save(u);
    }
}
