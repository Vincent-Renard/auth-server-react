package com.example.auth.server.model.entities.logs.user;

import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.enums.LogStatus;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @autor Vincent
 * @date 30/07/2020
 */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@ToString
public abstract class UserLog {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Enumerated(value = EnumType.STRING)
	LogStatus status;

	@CreationTimestamp
	LocalDateTime date;

	@Setter
	@ManyToOne
	@JsonIdentityReference(alwaysAsId = true)
	@ToString.Exclude
	Credentials user;

	protected UserLog(Credentials user, LogStatus status) {

		this.status = status;
		this.user = user;
	}


}
