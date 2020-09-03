package com.example.auth.server.authentification.facade.persistence.entities.logs;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.enums.LogStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.Collection;
import java.util.TreeSet;

/**
 * @autor Vincent
 * @date 16/08/2020
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString(callSuper = true)
@Entity
public class AdminRoleUpdateLog extends AdminLog {


    @Setter
    @ElementCollection
    Collection<String> roles = new TreeSet<>();


    public AdminRoleUpdateLog(Credentials userOn) {
        super(userOn, LogStatus.ADMIN_USER_ROLES_UPDATE);
    }
}
