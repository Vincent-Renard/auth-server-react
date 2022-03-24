package com.example.auth.server.model.entities;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @autor Vincent
 * @date 25/06/2020
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
public class ForbidenDomain {

	@Id
	@Column(length = 512, unique = true, nullable = false)
	String domain;

	@CreationTimestamp
	LocalDateTime dateTimeInserted;
	@Setter
	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JsonIdentityReference(alwaysAsId = true)
	Credentials admin;

	public ForbidenDomain(Credentials admin, String dom) {
		//this.admin=admin;
		this.domain = dom;
	}

	@Override
	public String toString() {
		return "ForbidenDomain{" +
				"domain='" + domain + '\'' +
				", dateTimeInserted=" + dateTimeInserted +
				", admin.id=" + (admin == null ? "null" : admin.getIdUser()) +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ForbidenDomain that = (ForbidenDomain) o;
		return domain.equals(that.domain);
	}

	@Override
	public int hashCode() {
		return Objects.hash(domain);
	}
}
