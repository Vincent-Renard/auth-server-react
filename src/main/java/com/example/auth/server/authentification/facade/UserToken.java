package com.example.auth.server.authentification.facade;

import com.example.auth.server.model.dtos.out.Bearers;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * @autor Vincent
 * @date 16/07/2020
 */

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserToken {
    long idUser;
    Bearers bearers;
}
