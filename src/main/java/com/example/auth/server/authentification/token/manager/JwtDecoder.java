package com.example.auth.server.authentification.token.manager;

import com.example.auth.server.authentification.token.TokenType;
import com.example.auth.server.model.KeyStore;
import com.example.auth.server.model.exceptions.InvalidToken;
import com.example.auth.server.model.exceptions.NoToken;
import com.example.auth.server.model.exceptions.TokenExpired;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor Vincent
 * @date 11/04/2020
 */

@Component
public class JwtDecoder implements TokenConstant {
    @Value(value = "${auth.token.prefix}")
    private String TOKENS_PREFIX;

    private final PublicKey publicKey = KeyStore.getInstance().getKeyChain().getPublic();

    private UsernamePasswordAuthenticationToken decodeRefresh(Claims refreshClaims) throws InvalidToken {
        try {

            long iduser = Long.parseLong(refreshClaims.getSubject());

            var upat = new UsernamePasswordAuthenticationToken(iduser, null, null);

            return upat;
        } catch (JwtException e) {
            e.printStackTrace();
            throw new InvalidToken();
        }
    }

    private UsernamePasswordAuthenticationToken decodeAccess(Claims accessClaims) throws InvalidToken {
        try {
            var userroles = accessClaims.get(CLAIMS_KEY_TOKEN_ROLES, List.class);
            List<String> roles = new ArrayList<>();
            userroles.forEach(r -> roles.add((String) r));

            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            long iduser = Long.parseLong(accessClaims.getSubject());

            var upat = new UsernamePasswordAuthenticationToken(iduser, null, authorities);
            return upat;

        } catch (JwtException e) {
            e.printStackTrace();
            throw new InvalidToken();
        }
    }

    public UsernamePasswordAuthenticationToken decode(String token) throws
            NoToken, InvalidToken, TokenExpired {
        if (token == null)
            throw new NoToken();
        if (token.startsWith(TOKENS_PREFIX))
            token = token.replace(TOKENS_PREFIX, "");
        try {
            Claims body = Jwts.parserBuilder().setSigningKey(publicKey).build()
                    .parseClaimsJws(token).getBody();

            TokenType type = TokenType.valueOf(body.get(CLAIMS_KEY_TOKEN_TYPE).toString());
            if (type.equals(TokenType.ACCESS))
                return decodeAccess(body);
            else return decodeRefresh(body);
        } catch (ExpiredJwtException e) {
            throw new TokenExpired();
        } catch (JwtException e) {
            throw new InvalidToken();
        }
    }
}

