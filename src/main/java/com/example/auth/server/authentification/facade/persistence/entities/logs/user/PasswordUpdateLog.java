package com.example.auth.server.authentification.facade.persistence.entities.logs.user;

import com.example.auth.server.authentification.facade.persistence.entities.enums.LogStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;

/**
 * @autor Vincent
 * @date 03/09/2020
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
public class PasswordUpdateLog extends UserLog {
    public PasswordUpdateLog() {
        super(LogStatus.UPDATE_PASSWORD);
    }
}
