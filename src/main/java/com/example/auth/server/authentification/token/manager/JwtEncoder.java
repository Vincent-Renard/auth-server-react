package com.example.auth.server.authentification.token.manager;

import com.example.auth.server.authentification.KeyStore;
import com.example.auth.server.authentification.facade.persistence.InternalMemory;
import com.example.auth.server.authentification.facade.persistence.entities.TokensId;
import com.example.auth.server.authentification.token.TokenType;
import com.example.auth.server.model.dtos.out.Bearers;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @autor Vincent
 * @date 22/03/2020
 */
@Component
public class JwtEncoder implements TokenConstant {

    @Autowired
    InternalMemory memory;

    private AtomicLong idAccessTokenGenerator;
    @Autowired
    private KeyStore keys;

    @Value(value = "${auth.token.issuer}")
    private String ISSUER;
    private AtomicLong idRefreshTokenGenerator;

    @PostConstruct
    public void init() {

        Optional<TokensId> last = memory.findLastTokensId();
        TokensId ids;
        if (last.isPresent()) {
            ids = last.get();

        } else {
            ids = new TokensId();
            ids.setIdAccessToken(1L);
            ids.setIdRefreshToken(1L);
            ids.setId(1L);
            memory.saveTokensId(ids);
        }
        idAccessTokenGenerator = new AtomicLong(ids.getIdAccessToken());
        idRefreshTokenGenerator = new AtomicLong(ids.getIdRefreshToken());
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
                .setId(String.valueOf(idAccessTokenGenerator.getAndIncrement()))
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + AUTH_MS_TTL));
        cls.put(CLAIMS_KEY_TOKEN_TYPE, TokenType.ACCESS);

        var r = new ArrayList<>(roles);
        cls.put(CLAIMS_KEY_TOKEN_ROLES, r);

        var t = memory.getLastTokensId();
        t.setIdAccessToken(idAccessTokenGenerator.get());
        memory.saveTokensId(t);

        return Jwts.builder()
                .setClaims(cls)
                .signWith(keys.getPrivateKey())
                .compact();

    }

    private String encodeRefresh(long iduser) {
        long now = System.currentTimeMillis();
        Claims cls = Jwts.claims()
                .setIssuer(ISSUER)
                .setId(String.valueOf(idRefreshTokenGenerator.getAndIncrement()))
                .setSubject(String.valueOf(iduser))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + REFR_MS_TTL));
        cls.put(CLAIMS_KEY_TOKEN_TYPE, TokenType.REFRESH);


        var t = memory.getLastTokensId();

        t.setIdRefreshToken(idRefreshTokenGenerator.get());
        memory.saveTokensId(t);
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
