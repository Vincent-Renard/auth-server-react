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
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
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
    private void genKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (keyChain == null) {
            System.err.println("kay null ");
            Optional<RSAPrivateKeyEntity> k = keyRepository.findById(1L);
            if (!k.isPresent()) {
                System.err.println("key gen ");
                keyChain = Keys.keyPairFor(SignatureAlgorithm.RS256);
                var sk = Base64.getEncoder().encodeToString(keyChain.getPrivate().getEncoded());


                keyRepository.save(new RSAPrivateKeyEntity(keyChain.getPrivate().getEncoded()));


            } else {
                byte[] skey = k.get().getKey();
                System.err.println("key " + Arrays.toString(skey));

                PKCS8EncodedKeySpec secretKeySpec = new PKCS8EncodedKeySpec(skey);

                var kes = KeyFactory.getInstance("RSA").generatePrivate(secretKeySpec);
                System.err.println(kes.toString());
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
