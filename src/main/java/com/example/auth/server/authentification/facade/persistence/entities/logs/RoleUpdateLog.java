package com.example.auth.server.authentification.facade.persistence.entities.logs;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Collection;
import java.util.TreeSet;

/**
 * @autor Vincent
 * @date 16/08/2020
 */
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@Entity
public class RoleUpdateLog extends UserLog {



    @Setter
    @ElementCollection
    Collection<String> roles = new TreeSet<>();

    @Setter
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonIdentityReference(alwaysAsId = true)
    Credentials admin;
}
