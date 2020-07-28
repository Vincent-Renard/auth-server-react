package com.example.auth.server.authentification.facade.persistence;

import com.example.auth.server.authentification.facade.persistence.entities.RSAKey;
import com.example.auth.server.authentification.facade.persistence.entities.TokensId;
import com.example.auth.server.authentification.facade.persistence.repositories.KeyRepository;
import com.example.auth.server.authentification.facade.persistence.repositories.TokenRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @autor Vincent
 * @date 28/07/2020
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InternalMemory {

    @Autowired
    KeyRepository keys;

    @Autowired
    TokenRepository tokens;

    public Optional<TokensId> findLastTokensId() {
        return tokens.findById(1L);
    }

    public TokensId saveTokensId(TokensId ids) {
        return tokens.save(ids);
    }

    public TokensId getLastTokensId() {
        return tokens.getOne(1L);
    }

    public Optional<RSAKey> findKeys() {
        return keys.findById(1L);
    }

    public RSAKey saveKeys(RSAKey rsaKey) {
        return keys.save(rsaKey);
    }
}
