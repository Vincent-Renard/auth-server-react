package com.example.auth.server.authentification.facade.persistence;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.enums.LogUserStatus;
import com.example.auth.server.authentification.facade.persistence.entities.logs.RoleUpdateLog;
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
        RoleUpdateLog entity = new RoleUpdateLog();
        entity.setRoles(newRoles);
        entity.setStatus(LogUserStatus.ROLES_UPDATE);
        entity.setUser(user);
        entity.setAdmin(admin);

        user.getLogs().add(entity);
        users.save(user);


    }
}
