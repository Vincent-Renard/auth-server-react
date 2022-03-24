package com.example.auth.server.model.entities.logs.user;

import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.enums.BanReason;
import com.example.auth.server.model.entities.enums.LogStatus;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * @autor Vincent
 * @date 03/09/2020
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
public class BanLog extends UserLog {
	@Enumerated
	@Setter
	BanReason reason;


	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIdentityReference(alwaysAsId = true)
	Credentials admin;


	public BanLog(Credentials user, Credentials admin, BanReason reason) {
		super(user, LogStatus.BAN);
		this.admin = admin;
		setReason(reason);
	}
}

