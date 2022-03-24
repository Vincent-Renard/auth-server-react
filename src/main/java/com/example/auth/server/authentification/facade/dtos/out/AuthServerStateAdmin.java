package com.example.auth.server.authentification.facade.dtos.out;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

/**
 * @autor Vincent
 * @date 22/07/2020
 */
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthServerStateAdmin extends AuthServerStatePublic {

	long nbUsersRegistered;
	long nbUsersAdminsRegistered;
	Collection<String> forbidenDomains;

}
