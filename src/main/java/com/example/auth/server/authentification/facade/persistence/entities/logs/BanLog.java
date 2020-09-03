package com.example.auth.server.authentification.facade.persistence.entities.logs;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.enums.LogStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * @autor Vincent
 * @date 16/08/2020
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@Entity
public class BanLog extends UserLog {

    @Setter
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    Credentials admin;

    public BanLog() {
        super(LogStatus.BAN);
    }
}
