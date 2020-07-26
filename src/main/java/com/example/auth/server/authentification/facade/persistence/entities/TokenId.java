package com.example.auth.server.authentification.facade.persistence.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @autor Vincent
 * @date 22/07/2020
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@Entity
public class TokenId {
    @Id
    @Setter
    Long id;

    @Setter
    Long idAccessToken;
    @Setter
    Long idRefreshToken;
}
