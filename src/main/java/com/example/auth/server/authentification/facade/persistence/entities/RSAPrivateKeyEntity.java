package com.example.auth.server.authentification.facade.persistence.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @autor Vincent
 * @date 29/04/2020
 */


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
@ToString
public class RSAPrivateKeyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(length = 4096)
    String key;

    LocalDateTime dateTimeInserted;

    public RSAPrivateKeyEntity() {
    }

    public RSAPrivateKeyEntity(String cle) {
        key = cle;
        dateTimeInserted = LocalDateTime.now();
    }


}
