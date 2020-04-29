package com.example.auth.server.model;

import com.example.auth.server.authentification.facade.persistence.entities.RSAPrivateKeyEntity;
import com.example.auth.server.authentification.facade.persistence.repositories.KeyRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Optional;

/**
 * @autor Vincent
 * @date 11/03/2020
 */
@Component
public class KeyStore {
    private static final Logger log = LoggerFactory.getLogger(KeyStore.class);

    private KeyPair keyChain;

    @Autowired
    private KeyRepository keyRepository;

    @PostConstruct
    private void genKey() {
        System.err.println("post construct ");
        if (keyChain == null) {
            System.err.println("kay null ");
            Optional<RSAPrivateKeyEntity> k = keyRepository.findById(1L);
            if (!k.isPresent()) {
                System.err.println("key gen ");
                keyChain = Keys.keyPairFor(SignatureAlgorithm.RS256);
                var sk = Base64.getEncoder().encodeToString(keyChain.getPublic().getEncoded());
                // System.err.println(sk);

                keyRepository.save(new RSAPrivateKeyEntity(sk));
            } else {
                String skey = k.get().getKey();
                log.debug(skey);
            }
            log.debug(keyChain.getPublic().toString());
            log.debug(Base64.getEncoder().encodeToString(keyChain.getPublic().getEncoded()));
        }

    }

    public PublicKey getPublicKey() {
        return keyChain.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyChain.getPrivate();
    }
}
