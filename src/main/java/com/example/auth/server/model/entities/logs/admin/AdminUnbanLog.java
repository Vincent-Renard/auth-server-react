package com.example.auth.server.model.entities.logs.admin;

import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.enums.LogStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;

/**
 * @autor Vincent
 * @date 03/09/2020
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
public class AdminUnbanLog extends AdminLog {


	public AdminUnbanLog(Credentials admin, Credentials user) {
		super(admin, user, LogStatus.ADMIN_UNBAN_USER);
	}
}

