package com.example.auth.server.authentification.facade.persistence.entities.logs;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.enums.LogStatus;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.*;
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
@Inheritance
@ToString
public class AdminLog extends UserLog {
    @Setter
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonIdentityReference(alwaysAsId = true)
    @ToString.Exclude
    Credentials userOn;

    public AdminLog(Credentials userOn, LogStatus status) {
        super(status);

        this.setUserOn(userOn);
    }

}
