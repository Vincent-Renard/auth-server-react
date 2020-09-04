package com.example.auth.server.authentification.facade.persistence.entities.logs.admin;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.enums.LogStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;

/**
 * @autor Vincent
 * @date 03/09/2020
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
public class AdminUnbanUserLog extends AdminLog {


    public AdminUnbanUserLog(Credentials admin, Credentials user) {
        super(admin, user, LogStatus.ADMIN_UNBAN_USER);
    }
}

