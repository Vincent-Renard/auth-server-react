package com.example.auth.server.authentification.token.manager;

import com.example.auth.server.authentification.token.TokenType;
import com.example.auth.server.model.KeyStore;
import com.example.auth.server.model.dtos.out.Bearers;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @autor Vincent
 * @date 22/03/2020
 */

public class JwtEncoder implements TokenConstant {
    private static final long AUTH_MS_TTL = 3_600_000;

    private final KeyStore keys;
    private final AtomicLong idTokenGenerator;
    private static final long REFR_MS_TTL = 86_400_000;
    @Value(value = "${auth.token.issuer}")
    private String ISSUER;

    public JwtEncoder() {
        keys = KeyStore.getInstance();
        idTokenGenerator = new AtomicLong(1L);

    }

    public RSAPublicKey getPublicKey() {

        return (RSAPublicKey) keys.getKeyChain().getPublic();
    }

    private String encodeAccess(long id, List<String> roles) {

        long now = System.currentTimeMillis();
        Claims cls = Jwts.claims()
                .setIssuer(ISSUER)
                .setId(String.valueOf(idTokenGenerator.getAndIncrement()))
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + AUTH_MS_TTL));
        cls.put(CLAIMS_KEY_TOKEN_TYPE, TokenType.ACCESS);
        List<String> r = roles.stream()
                .map(String::new)
                .collect(Collectors.toList());
        cls.put(CLAIMS_KEY_TOKEN_ROLES, r);


        String token = Jwts.builder()
                .setClaims(cls)
                .signWith(keys.getKeyChain().getPrivate())
                .compact();

        return token;

    }

    private String encodeRefresh(long iduser) {
        long now = System.currentTimeMillis();
        Claims cls = Jwts.claims()
                .setIssuer(ISSUER)
                .setId(String.valueOf(idTokenGenerator.getAndIncrement()))
                .setSubject(String.valueOf(iduser))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + REFR_MS_TTL));
        cls.put(CLAIMS_KEY_TOKEN_TYPE, TokenType.REFRESH);


        String token = Jwts.builder()
                .setClaims(cls)
                .signWith(keys.getKeyChain().getPrivate())
                .compact();

        return token;
    }

    public Bearers genBoth(long iduser, List<String> roles) {
        var at = encodeAccess(iduser, roles);
        var rt = encodeRefresh(iduser);
        return Bearers.from(at,rt);
    }

}
