package com.example.auth.server.model.entities;

import com.example.auth.server.model.entities.logs.user.UserLog;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @autor Vincent
 * @date 12/03/2020
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idUser")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Credentials {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long idUser;

	@Column(unique = true, nullable = false)
	@Setter
	@Email
	String mail;

	@CreationTimestamp
	LocalDateTime inscriptionDate;


	@Column(nullable = false)
	@Setter
	String password;


	@Setter
	@ElementCollection
	Collection<String> roles = new TreeSet<>();

	@UpdateTimestamp
	LocalDateTime updateDate;

	@Setter
	@OneToOne(cascade = CascadeType.ALL, optional = true, mappedBy = "user")
	private Banishment banishment;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@Setter
	@JsonManagedReference
	List<UserLog> logs = new ArrayList<>();


	public Credentials(String mail, String password, Collection<String> roles) {

		this.mail = mail.toLowerCase();
		this.password = password;
		this.roles = roles;

	}

	public void addLog(UserLog log) {
		this.getLogs().add(log);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Credentials that = (Credentials) o;
		return Objects.equals(idUser, that.idUser) && mail.equals(that.mail);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idUser, mail);
	}

	public String toString() {
		return "Credentials{" +
				"idUser=" + idUser +
				", mail='" + mail + '\'' +
				", inscriptionDate=" + inscriptionDate +
				", password='" + password + '\'' +
				", roles=" + roles +
				", updateDate=" + updateDate +
				", banishment=" + ((banishment == null) ? "null" : banishment.toString()) +
				", logs=" + logs +
				'}';
	}


}
