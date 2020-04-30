package com.example.auth.server.authentification.token.manager;

import com.example.auth.server.authentification.KeyStore;
import com.example.auth.server.authentification.token.TokenType;
import com.example.auth.server.model.dtos.out.Bearers;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @autor Vincent
 * @date 22/03/2020
 */
@Component
public class JwtEncoder implements TokenConstant {
    private static final long AUTH_MS_TTL = 3_600_000;
    private static final long REFR_MS_TTL = 86_400_000;
    private final AtomicLong idTokenGenerator;
    @Autowired
    private KeyStore keys;
    @Value(value = "${auth.token.issuer}")
    private String ISSUER;

    public JwtEncoder() {
        idTokenGenerator = new AtomicLong(1L);

    }

    public RSAPublicKey getPublicKey() {

        return (RSAPublicKey) keys.getPublicKey();
    }

    private String encodeAccess(long id, Set<String> roles) {

        long now = System.currentTimeMillis();
        Claims cls = Jwts.claims()
                .setIssuer(ISSUER)
                .setId(String.valueOf(idTokenGenerator.getAndIncrement()))
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + AUTH_MS_TTL));
        cls.put(CLAIMS_KEY_TOKEN_TYPE, TokenType.ACCESS);
        Set<String> r = roles.stream()
                .map(String::new)
                .collect(Collectors.toSet());
        cls.put(CLAIMS_KEY_TOKEN_ROLES, r);


        return Jwts.builder()
                .setClaims(cls)
                .signWith(keys.getPrivateKey())
                .compact();

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


        return Jwts.builder()
                .setClaims(cls)
                .signWith(keys.getPrivateKey())
                .compact();
    }

    public Bearers genBoth(long iduser, Set<String> roles) {
        var at = encodeAccess(iduser, roles);
        var rt = encodeRefresh(iduser);
        return Bearers.from(at, rt);
    }

}
