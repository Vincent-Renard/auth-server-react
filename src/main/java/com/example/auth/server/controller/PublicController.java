package com.example.auth.server.controller;

import com.example.auth.server.authentification.facade.AuthService;
import com.example.auth.server.model.dtos.out.PubKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @autor Vincent
 * @date 16/07/2020
 */

@RestController
@RequestMapping("/auth")
public class PublicController {
    @Autowired
    private AuthService base;

    @GetMapping(value = "/public", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PubKey> pubkey() {
        return ResponseEntity.ok(PubKey.from(base.publicKey().getEncoded()));
    }

}
