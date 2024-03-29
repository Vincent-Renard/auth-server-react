package com.example.auth.server.authentification.facade.dtos.out;

import lombok.Value;

/**
 * @autor Vincent
 * @date 12/03/2020
 */
@Value(staticConstructor = "from")
public class Bearers {
	String accessToken;
	String refreshToken;
}
