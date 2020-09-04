package com.example.auth.server.authentification.facade.persistence.entities.logs.user;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.enums.LogStatus;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Collection;
import java.util.TreeSet;

/**
 * @autor Vincent
 * @date 16/08/2020
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
public class RoleUpdateLog extends UserLog {


    @Setter
    @ElementCollection
    Collection<String> roles = new TreeSet<>();

    @Setter
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonIdentityReference(alwaysAsId = true)
    Credentials admin;

    public RoleUpdateLog(Credentials user) {
        super(user, LogStatus.ROLES_UPDATE);
    }

    @Override
    public String toString() {
        return "RoleUpdateLog{" +
                "roles=" + roles +
                ", admin=" + admin.getIdUser() +
                "} " + super.toString();
    }
}
