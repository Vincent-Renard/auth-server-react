package com.example.auth.server.model.entities;

import com.example.auth.server.model.entities.enums.BanReason;
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
 * @date 26/06/2020
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
public class Banishment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Enumerated(value = EnumType.STRING)
	BanReason reason;

	@CreationTimestamp
	LocalDateTime date;


	@Setter
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIdentityReference(alwaysAsId = true)
	Credentials admin;

	@Setter
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIdentityReference(alwaysAsId = true)
	Credentials user;

	public Banishment(Credentials admin, BanReason reason) {
		this.reason = reason;
		this.admin = admin;
	}

	@Override
	public String toString() {
		return "Banishment{" +
				"id=" + id +
				", user.id= " + ((user != null) ? user.getIdUser() : "null") +
				", reason=" + reason +
				", admin.id= " + ((admin != null) ? admin.getIdUser() : "null") +
				", date=" + date +

				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Banishment that = (Banishment) o;
		return Objects.equals(id, that.id) && reason == that.reason && date.equals(that.date) && Objects.equals(admin, that.admin) && user.equals(that.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, reason, date, admin, user);
	}
}
