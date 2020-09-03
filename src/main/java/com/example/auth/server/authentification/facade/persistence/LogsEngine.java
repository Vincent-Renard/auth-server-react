package com.example.auth.server.authentification.facade.persistence;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.logs.AdminRoleUpdateLog;
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
        //logUser.setUser(user);
        logUser.setAdmin(admin);
        user.getLogs().add(logUser);

        //user=
        var t = users.save(user);
        System.err.println(t.toString());

        AdminRoleUpdateLog logAdmin = new AdminRoleUpdateLog();
        logAdmin.setRoles(newRoles);
        //logAdmin.setUser(admin);
        logAdmin.setUserOn(user);
        admin.getLogs().add(logAdmin);

        var u = users.save(admin);
        System.err.println(u.toString());
    }


    public void logBadPassword(Long idUser) {
        //TODO
    }

    public void logUserLogin(Long idUser) {

    }
}
