package com.example.auth.server.model.dtos.out;

import com.example.auth.server.authentification.facade.persistence.entities.BanReason;
import com.example.auth.server.authentification.facade.persistence.entities.BanishmentEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * @autor Vincent
 * @date 26/06/2020
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Getter
@Setter
public class UserBanishment {
    BanReason reason;
    LocalDateTime date;

    public static UserBanishment from(BanishmentEntity b) {
        return new UserBanishment(b.getReason(), b.getDate());
    }
}
