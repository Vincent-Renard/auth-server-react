package com.example.auth.server.model.entities.logs.admin;

import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.enums.LogStatus;
import com.example.auth.server.model.entities.logs.user.UserLog;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * @autor Vincent
 * @date 03/09/2020
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
@ToString
public abstract class AdminLog extends UserLog {

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIdentityReference(alwaysAsId = true)
    @ToString.Exclude
    Credentials userOn;


    public AdminLog(Credentials userAdmin, Credentials userOn, LogStatus status) {
        super(userAdmin, status);
        this.setUserOn(userOn);
    }

}
