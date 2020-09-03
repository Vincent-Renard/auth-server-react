package com.example.auth.server.authentification.facade.persistence;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.logs.AdminRoleUpdateLog;
import com.example.auth.server.authentification.facade.persistence.entities.logs.RegistrationLog;
import com.example.auth.server.authentification.facade.persistence.entities.logs.UserLogingLog;
import com.example.auth.server.authentification.facade.persistence.entities.logs.UserRoleUpdateLog;
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
        UserRoleUpdateLog logUser = new UserRoleUpdateLog();

        logUser.setRoles(newRoles);
        logUser.setAdmin(admin);
        user.getLogs().add(logUser);

        users.save(user);

        AdminRoleUpdateLog logAdmin = new AdminRoleUpdateLog();
        logAdmin.setRoles(newRoles);
        logAdmin.setUserOn(user);
        admin.getLogs().add(logAdmin);

        users.save(admin);
    }


    public void logBadPassword(Long idUser) {
        //TODO
    }

    public void logUserLogin(Credentials user) {
        UserLogingLog ull = new UserLogingLog();
        user.getLogs().add(ull);
        users.save(user);

    }

    public void logRegistration(Credentials user) {
        user.getLogs().add(new RegistrationLog());
        users.save(user);
    }
}
