package com.example.auth.server.authentification.facade.services.server;

import com.example.auth.server.authentification.facade.dtos.out.AuthServerStatePublic;

import java.security.PublicKey;

public interface ServerService {


	PublicKey publicKey();

	void clear();

	AuthServerStatePublic getServerStatePublic();


}
