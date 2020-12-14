package com.example.auth.server.authentification.facade.persistence.entities.logs.user;

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
public class ClaimResetTokenLog extends UserLog {

    public ClaimResetTokenLog(Credentials user) {
        super(user, LogStatus.CLAIM_PASSWORD_RESET_TOKEN);
    }
}


