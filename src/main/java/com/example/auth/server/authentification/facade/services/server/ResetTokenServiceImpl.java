package com.example.auth.server.authentification.facade.services.server;

import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.ResetPasswordToken;
import com.example.auth.server.model.repositories.ResetPasswordTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ResetTokenServiceImpl implements ResetTokenService {

	final ResetPasswordTokenRepository resetPasswordTokenRepository;

	@Override
	public ResetPasswordToken generateResetToken(Credentials u) {

		String key;
		do {
			key = UUID.randomUUID().toString().replace("-", "");
		} while (resetPasswordTokenRepository.existsByResetToken(key));

		return resetPasswordTokenRepository.save(new ResetPasswordToken(key, u));
	}

}
