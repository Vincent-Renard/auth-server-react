package com.example.auth.server.authentification.token.manager;

/**
 * @autor Vincent
 * @date 22/03/2020
 */
public interface TokenConstant {
	long AUTH_MS_TTL = 3_600_000;
	long REFR_MS_TTL = 86_400_000;
	String CLAIMS_KEY_TOKEN_ROLES = "roles";
	String CLAIMS_KEY_TOKEN_TYPE = "type";
}
