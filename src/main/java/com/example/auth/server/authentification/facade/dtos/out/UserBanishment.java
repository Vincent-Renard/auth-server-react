package com.example.auth.server.authentification.facade.dtos.out;

import com.example.auth.server.model.entities.Banishment;
import com.example.auth.server.model.entities.enums.BanReason;
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

	public static UserBanishment from(Banishment b) {
		return new UserBanishment(b.getReason(), b.getDate());
	}
}
