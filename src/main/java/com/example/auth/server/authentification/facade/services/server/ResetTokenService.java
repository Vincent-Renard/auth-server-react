package com.example.auth.server.authentification.facade.services.server;

import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.ResetPasswordToken;

public interface ResetTokenService {


	ResetPasswordToken generateResetToken(Credentials u);
}
