package com.example.auth.server.authentification.facade.dtos.out;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * @autor Vincent
 * @date 22/07/2020
 */
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthServerStatePublic {

    LocalDateTime startDateServer;
    byte[] key;
    long authTokenTTL;
    long refreshTokenTTL;

}
