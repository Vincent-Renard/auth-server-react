package com.example.auth.server.model;

import com.example.auth.server.model.entities.RSAKey;
import com.example.auth.server.model.entities.TokensId;
import com.example.auth.server.model.repositories.KeyRepository;
import com.example.auth.server.model.repositories.TokenRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @autor Vincent
 * @date 28/07/2020
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class InternalMemory {

	KeyRepository keys;
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
