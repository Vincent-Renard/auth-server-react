package com.example.auth.server.authentification.facade.persistence.entities.logs.user;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.enums.BanReason;
import com.example.auth.server.authentification.facade.persistence.entities.enums.LogStatus;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

/**
 * @autor Vincent
 * @date 03/09/2020
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
public class BanUserLog extends UserLog {
    @Enumerated
    @Setter
    BanReason reason;


    //@Setter
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIdentityReference(alwaysAsId = true)
    Credentials admin;


    public BanUserLog(Credentials user, Credentials admin, BanReason reason) {
        super(user, LogStatus.BAN);
        this.admin = admin;
        setReason(reason);

    }
}

