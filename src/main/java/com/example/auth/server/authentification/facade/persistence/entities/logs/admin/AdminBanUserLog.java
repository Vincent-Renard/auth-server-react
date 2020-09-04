package com.example.auth.server.authentification.facade.persistence.entities.logs.admin;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.enums.BanReason;
import com.example.auth.server.authentification.facade.persistence.entities.enums.LogStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Enumerated;

/**
 * @autor Vincent
 * @date 03/09/2020
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
public class AdminBanUserLog extends AdminLog {
    @Enumerated
    @Setter
    BanReason reason;


    public AdminBanUserLog(Credentials admin, Credentials user, BanReason reason) {
        super(admin, user, LogStatus.ADMIN_BAN_USER);
        setReason(reason);
    }
}

