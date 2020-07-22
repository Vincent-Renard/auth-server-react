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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @autor Vincent
 * @date 22/03/2020
 */
@Component
public class JwtEncoder implements TokenConstant {

    private final AtomicLong idTokenGenerator;
    @Autowired
    private KeyStore keys;
    @Value(value = "${auth.token.issuer}")
    private String ISSUER;

    public JwtEncoder() {
        idTokenGenerator = new AtomicLong(1L);

    }

    public long getAuthTTL() {
        return AUTH_MS_TTL;
    }

    public long getRefreshTTL() {
        return REFR_MS_TTL;
    }

    public RSAPublicKey getPublicKey() {

        return (RSAPublicKey) keys.getPublicKey();
    }

    private String encodeAccess(long id, Collection<String> roles) {
        long now = System.currentTimeMillis();
        Claims cls = Jwts.claims()
                .setIssuer(ISSUER)
                .setId(String.valueOf(idTokenGenerator.getAndIncrement()))
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + AUTH_MS_TTL));
        cls.put(CLAIMS_KEY_TOKEN_TYPE, TokenType.ACCESS);

        var r = new ArrayList<>(roles);
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

    public Bearers genBoth(long iduser, Collection<String> roles) {
        var at = encodeAccess(iduser, roles);
        var rt = encodeRefresh(iduser);
        return Bearers.from(at, rt);
    }

}
