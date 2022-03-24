package com.example.auth.server.model.entities.logs.admin;

import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.enums.BanReason;
import com.example.auth.server.model.entities.enums.LogStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Enumerated;

/**
 * @autor Vincent
 * @date 03/09/2020
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
public class AdminBanLog extends AdminLog {
	@Enumerated
	@Setter
	BanReason reason;


	public AdminBanLog(Credentials admin, Credentials user, BanReason reason) {
		super(admin, user, LogStatus.ADMIN_BAN_USER);
		setReason(reason);
	}
}

