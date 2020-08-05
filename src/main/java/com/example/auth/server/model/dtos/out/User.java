package com.example.auth.server.model.dtos.out;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

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

    private
    long id;
    private String mail;
    private LocalDateTime inscriptionDate;
    private Collection<String> roles;
    private LocalDateTime updateDate;
    private UserBanishment userBanishment;

    public static User from(Credentials u) {
        return new User(u.getIdUser(),
                u.getMail(),
                u.getInscriptionDate(),
                u.getRoles(),
                u.getUpdateDate(), u.getBanishment() == null ? null : UserBanishment.from(u.getBanishment()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
