package com.example.auth.server.authentification;

import com.example.auth.server.authentification.facade.persistence.InternalMemory;
import com.example.auth.server.authentification.facade.persistence.entities.RSAKey;
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
import java.security.spec.X509EncodedKeySpec;
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
    private InternalMemory memory;

    @PostConstruct
    private void genKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (keyChain == null) {
            Optional<RSAKey> k = memory.findKeys();
            if (!k.isPresent()) {
                keyChain = Keys.keyPairFor(SignatureAlgorithm.RS256);
                var sk = Base64.getEncoder().encodeToString(keyChain.getPrivate().getEncoded());

                var pk = Base64.getEncoder().encodeToString(keyChain.getPublic().getEncoded());

                memory.saveKeys(new RSAKey(sk, pk));


            } else {
                String sk = k.get().getPrivateKey();
                String pk = k.get().getPublicKey();
                byte[] pkcs8EncodedBytes = Base64.getDecoder().decode(sk);
                byte[] X509EncodedByes = Base64.getDecoder().decode(pk);

                PKCS8EncodedKeySpec secretKeySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
                X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(X509EncodedByes);

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                var privateKey = keyFactory.generatePrivate(secretKeySpec);
                var publicKey = keyFactory.generatePublic(publicKeySpec);


                keyChain = new KeyPair(publicKey, privateKey);


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
