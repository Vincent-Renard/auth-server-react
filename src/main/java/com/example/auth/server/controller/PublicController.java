package com.example.auth.server.controller;

import com.example.auth.server.authentification.facade.dtos.out.AuthServerStatePublic;
import com.example.auth.server.authentification.facade.dtos.out.ForbidenDomain;
import com.example.auth.server.authentification.facade.dtos.out.PubKey;
import com.example.auth.server.authentification.facade.services.server.DomainService;
import com.example.auth.server.authentification.facade.services.server.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @autor Vincent
 * @date 16/07/2020
 */

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class PublicController {

	private ServerService serverService;
	private DomainService domainService;

	@GetMapping(value = "/state", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<AuthServerStatePublic> state() {
		return ResponseEntity.ok(serverService.getServerStatePublic());
	}

	@GetMapping(value = "/public", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<PubKey> pubkey() {
		return ResponseEntity.ok(PubKey.from(serverService.publicKey().getEncoded()));
	}


	@GetMapping(value = "/domains", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Collection<ForbidenDomain>> getDomains() {
		return ResponseEntity
				.ok(domainService.getAllDomainNotAllowed()
						.stream().map(ForbidenDomain::from)
						.collect(Collectors.toList()));

	}

}
