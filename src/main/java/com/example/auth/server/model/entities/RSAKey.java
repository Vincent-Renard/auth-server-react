package com.example.auth.server.model.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @autor Vincent
 * @date 29/04/2020
 */


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
@ToString
public class RSAKey {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@Column(length = 2048, unique = true, nullable = false)
	String privateKey;

	@Column(length = 2048, unique = true, nullable = false)
	String publicKey;

	@CreationTimestamp
	LocalDateTime dateTimeInserted;

	public RSAKey() {
	}

	public RSAKey(String sk, String pk) {
		privateKey = sk;
		publicKey = pk;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RSAKey rsaKey = (RSAKey) o;
		return privateKey.equals(rsaKey.privateKey) && publicKey.equals(rsaKey.publicKey);
	}

	@Override
	public int hashCode() {
		return Objects.hash(privateKey, publicKey);
	}
}
