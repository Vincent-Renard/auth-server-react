package com.example.auth.server.authentification.facade.persistence.entities;

import com.example.auth.server.authentification.facade.persistence.UserRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @autor Vincent
 * @date 12/03/2020
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@Entity
public class StoreUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long idUser;


    @Column(unique = true, nullable = false)
    @Setter
    String mail;
    LocalDateTime inscriptionDate;
    @Column(nullable = false)
    @Setter
    char[] password;

    @Setter
    Set<UserRole> roles;
    @ElementCollection
    Set<String> roles;
    @Setter
    LocalDateTime updateDate;

    public StoreUser(String mail, char[] password, Set<UserRole> roles) {
        this.mail = mail;
    }
    public StoreUser() {
    }

    public StoreUser(String mail, char[] password, Set<String> roles) {
        this.mail = mail.toLowerCase();
        this.password = password;
        this.roles = roles;
        inscriptionDate = LocalDateTime.now();
        updateDate = inscriptionDate;
    }
}
