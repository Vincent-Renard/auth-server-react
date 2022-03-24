package com.example.auth.server.authentification.token.manager;

import com.example.auth.server.authentification.KeyStore;
import com.example.auth.server.authentification.facade.exceptions.InvalidTokenException;
import com.example.auth.server.authentification.facade.exceptions.NoTokenException;
import com.example.auth.server.authentification.facade.exceptions.TokenExpiredException;
import com.example.auth.server.authentification.token.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor Vincent
 * @date 11/04/2020
 */

@Component
@RequiredArgsConstructor
public class JwtDecoder implements TokenConstant {
	final KeyStore keyStore;

	@Value(value = "${auth.token.prefix}")
	private String tokensPrefix;

	public long decodeRefreshToken(String token) throws NoTokenException, InvalidTokenException, TokenExpiredException {
		var upat = this.decode(token);


		var body = Jwts.parserBuilder().setSigningKey(keyStore.getPublicKey()).build()
				.parseClaimsJws(token).getBody();

		if (!body.get(CLAIMS_KEY_TOKEN_TYPE).equals(TokenType.REFRESH.name())) {
			throw new InvalidTokenException();
		}
		return (long) upat.getPrincipal();
	}

	private UsernamePasswordAuthenticationToken decodeRefresh(Claims refreshClaims) throws InvalidTokenException {
		try {

			long iduser = Long.parseLong(refreshClaims.getSubject());

			return new UsernamePasswordAuthenticationToken(iduser, null, null);
		} catch (JwtException e) {
			e.printStackTrace();
			throw new InvalidTokenException();
		}
	}

	private UsernamePasswordAuthenticationToken decodeAccess(Claims accessClaims) throws InvalidTokenException {
		try {
			var userroles = accessClaims.get(CLAIMS_KEY_TOKEN_ROLES, List.class);
			var roles = new ArrayList<String>();
			userroles.forEach(r -> roles.add((String) r));

			Collection<SimpleGrantedAuthority> authorities = roles.stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toSet());
			long iduser = Long.parseLong(accessClaims.getSubject());

			return new UsernamePasswordAuthenticationToken(iduser, null, authorities);

		} catch (JwtException e) {
			e.printStackTrace();
			throw new InvalidTokenException();
		}
	}

	public UsernamePasswordAuthenticationToken decode(String token) throws
			NoTokenException, InvalidTokenException, TokenExpiredException {
		if (token == null)
			throw new NoTokenException();
		if (token.startsWith(tokensPrefix))
			token = token.replace(tokensPrefix, "");
		try {
			Claims body = Jwts.parserBuilder().setSigningKey(keyStore.getPublicKey()).build()
					.parseClaimsJws(token).getBody();

			var type = TokenType.valueOf(body.get(CLAIMS_KEY_TOKEN_TYPE).toString());
			if (type.equals(TokenType.ACCESS))
				return decodeAccess(body);
			else return decodeRefresh(body);
		} catch (ExpiredJwtException e) {
			throw new TokenExpiredException();
		} catch (JwtException e) {
			throw new InvalidTokenException();
		}
	}
}

