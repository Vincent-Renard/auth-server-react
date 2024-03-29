package com.example.auth.server.model.entities.logs.user;

import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.enums.LogStatus;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.Collection;
import java.util.TreeSet;

/**
 * @autor Vincent
 * @date 16/08/2020
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
public class RoleUpdateLog extends UserLog {


	@Setter
	@ElementCollection
	Collection<String> roles = new TreeSet<>();

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIdentityReference(alwaysAsId = true)
	Credentials admin;

	public RoleUpdateLog(Credentials user) {
		super(user, LogStatus.ROLES_UPDATE);
	}

	@Override
	public String toString() {
		return "RoleUpdateLog{" +
				"roles=" + roles +
				", admin=" + admin.getIdUser() +
				"} " + super.toString();
	}
}
