package com.example.auth.server.model.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @autor Vincent
 * @date 22/07/2020
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@Entity
@EqualsAndHashCode(of = {"id"})
public class TokensId {
	@Id
	@Setter
	Long id;

	@Setter
	Long idAccessToken;
	@Setter
	Long idRefreshToken;

}
