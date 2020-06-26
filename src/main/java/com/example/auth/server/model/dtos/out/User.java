package com.example.auth.server.model.dtos.out;

import com.example.auth.server.authentification.facade.persistence.entities.UserEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @autor Vincent
 * @date 14/04/2020
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Getter
@Setter
public class User {
    long id;
    LocalDateTime inscriptionDate;
    Collection<String> roles;
    LocalDateTime updateDate;
    UserBanishment userBanishment;

    public static User from(UserEntity u) {
        return new User(u.getIdUser(), u.getInscriptionDate(),
                u.getRoles(),
                u.getUpdateDate(), u.getBanishment() == null ? null : UserBanishment.from(u.getBanishment()));
    }
}
