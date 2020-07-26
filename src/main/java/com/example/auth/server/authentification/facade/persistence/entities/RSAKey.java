package com.example.auth.server.authentification.facade.persistence.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

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
public class RSAKey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(length = 2048, unique = true, nullable = false)
    String privateKey;

    @Column(length = 2048, unique = true, nullable = false)
    String publicKey;

    @CreationTimestamp
    LocalDateTime dateTimeInserted;

    public RSAKey() {
    }

    public RSAKey(String sk, String pk) {
        privateKey = sk;
        publicKey = pk;
    }


}
