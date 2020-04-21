package com.example.auth.server.model;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.util.Base64;

/**
 * @autor Vincent
 * @date 11/03/2020
 */
public class KeyStore {
    private static final Logger log = LoggerFactory.getLogger(KeyStore.class);

    private static KeyStore instance;
    @Getter
    private final KeyPair keyChain;

    private KeyStore() {
        keyChain = Keys.keyPairFor(SignatureAlgorithm.RS256);
        log.debug(keyChain.getPublic().toString());
        log.debug(Base64.getEncoder().encodeToString(keyChain.getPublic().getEncoded()));
    }

    public static KeyStore getInstance() {
        if (instance == null) {
            instance = new KeyStore();
        }
        return instance;
    }

}
