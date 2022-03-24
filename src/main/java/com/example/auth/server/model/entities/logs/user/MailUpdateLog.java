package com.example.auth.server.model.entities.logs.user;

import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.enums.LogStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class MailUpdateLog extends UserLog {

	@Setter
	String oldMail;


	public MailUpdateLog(Credentials user, String oldMail) {
		super(user, LogStatus.UPDATE_MAIL);
		setOldMail(oldMail);
	}
}
