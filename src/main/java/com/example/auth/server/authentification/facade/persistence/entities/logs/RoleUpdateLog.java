package com.example.auth.server.authentification.facade.persistence.entities.logs;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Setter
    @ElementCollection
    Collection<String> roles = new TreeSet<>();

    @CreationTimestamp
    LocalDateTime updateDate;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    Credentials user;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    Credentials admin;
}
