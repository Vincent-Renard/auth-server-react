package com.example.auth.server.authentification.facade.persistence.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.TreeSet;

/**
 * @autor Vincent
 * @date 12/03/2020
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@Entity
@Table(name = "users")
public class StoreUser {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idUser;

    @Column(unique = true, nullable = false)
    @Setter
    @Email
    String mail;

    @NotNull
    LocalDateTime inscriptionDate;

    @Column(nullable = false)
    @Setter
    String password;


    @Setter
    @ElementCollection
    Collection<String> roles = new TreeSet<>();

    @Setter
    LocalDateTime updateDate;

    protected StoreUser() {
    }

    public StoreUser(String mail, String password, Collection<String> roles) {

        this.mail = mail.toLowerCase();
        this.password = password;
        this.roles = roles;
        inscriptionDate = LocalDateTime.now();
        updateDate = inscriptionDate;
    }
}
