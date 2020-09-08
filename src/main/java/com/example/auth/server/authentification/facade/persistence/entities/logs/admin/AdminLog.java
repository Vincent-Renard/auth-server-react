package com.example.auth.server.authentification.facade.persistence.entities.logs.admin;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.enums.LogStatus;
import com.example.auth.server.authentification.facade.persistence.entities.logs.user.UserLog;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

/**
 * @autor Vincent
 * @date 03/09/2020
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
@Inheritance
@ToString
public abstract class AdminLog extends UserLog {

/*    @Setter
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIdentityReference(alwaysAsId = true)
   // @OnDelete(action = OnDeleteAction.CASCADE)

    @ToString.Exclude
    Credentials userOn;
    */

    public AdminLog(Credentials userAdmin, Credentials userOn, LogStatus status) {
        super(userAdmin, status);
        //this.setUserOn(userOn);
    }

}
