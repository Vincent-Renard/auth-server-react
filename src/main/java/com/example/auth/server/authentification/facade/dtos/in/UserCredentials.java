package com.example.auth.server.authentification.facade.dtos.in;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @autor Vincent
 * @date 22/03/2020
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserCredentials {

    String mail;
    String password;

}
