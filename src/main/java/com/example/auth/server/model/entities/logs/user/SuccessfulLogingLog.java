package com.example.auth.server.model.entities.logs.user;

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
public class SuccessfulLogingLog extends UserLog {

	public SuccessfulLogingLog(Credentials user) {
		super(user, LogStatus.LOGING_SUCCESSFULL);
	}
}
