package com.example.auth.server.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @autor Vincent
 * @date 12/03/2020
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
public class StoreUser {
    @Setter
    long idUser;
    @Setter
    String mail;
    final LocalDateTime inscriptionDate;
    @Setter
    char[] password;
    @Setter
    Set<String> roles;
    @Setter
    LocalDateTime updateDate;

    public StoreUser(String mail, char[] password, Set<String> roles) {
        this.mail = mail;
        this.password = password;
        this.roles = roles;
        inscriptionDate = LocalDateTime.now();
        updateDate = inscriptionDate;
    }
}
