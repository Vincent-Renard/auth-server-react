package com.example.auth.server.authentification.facade.persistence;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.enums.BanReason;
import com.example.auth.server.authentification.facade.persistence.entities.logs.admin.AdminBanUserLog;
import com.example.auth.server.authentification.facade.persistence.entities.logs.admin.AdminRoleUpdateLog;
import com.example.auth.server.authentification.facade.persistence.entities.logs.admin.AdminUnbanUserLog;
import com.example.auth.server.authentification.facade.persistence.entities.logs.user.*;
import com.example.auth.server.authentification.facade.persistence.repositories.CredentialsRepository;
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

    public void logRoleUpdate(Credentials user, Credentials admin, Collection<String> newRoles) {
        RoleUpdateLog logUser = new RoleUpdateLog();

        logUser.setRoles(newRoles);
        logUser.setAdmin(admin);
        user.getLogs().add(logUser);

        users.save(user);

        AdminRoleUpdateLog logAdmin = new AdminRoleUpdateLog(user);
        logAdmin.setRoles(newRoles);
        admin.getLogs().add(logAdmin);

        users.save(admin);
    }


    public void logBadPassword(Credentials user) {
        BadPasswordAttempt bpal = new BadPasswordAttempt();
        user.getLogs().add(bpal);
        users.save(user);
    }

    public void logLogin(Credentials user) {
        SuccessfulLogingLog ull = new SuccessfulLogingLog();
        user.getLogs().add(ull);
        users.save(user);

    }

    public void logRegistration(Credentials user) {
        user.getLogs().add(new RegistrationLog());
        users.save(user);
    }

    public void LogRefreshing(Credentials user) {
        user.getLogs().add(new RefreshingTokensLog());
        users.save(user);
    }

    public void logUpdatePassword(Credentials user) {
        user.getLogs().add(new PasswordUpdateLog());
        users.save(user);
    }

    public void logBan(Credentials user, Credentials admin, BanReason reason) {
        BanUserLog bul = new BanUserLog(admin, reason);
        user.getLogs().add(bul);
        users.save(user);


        AdminBanUserLog abul = new AdminBanUserLog(user, reason);
        admin.getLogs().add(abul);
        users.save(admin);

    }

    public void logMailUpdate(String oldMail, Credentials user) {
        MailUpdateLog mul = new MailUpdateLog(oldMail);
        user.getLogs().add(mul);
        users.save(user);

    }

    public void logUnban(Credentials user, Credentials admin) {
        UnbanLog bul = new UnbanLog(admin);
        user.getLogs().add(bul);
        users.save(user);


        AdminUnbanUserLog abul = new AdminUnbanUserLog(user);
        admin.getLogs().add(abul);
        users.save(admin);
    }
}
