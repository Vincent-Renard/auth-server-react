package com.example.auth.server.controller;

import com.example.auth.server.authentification.facade.dtos.in.RefreshRequest;
import com.example.auth.server.authentification.facade.dtos.in.UserCredentials;
import com.example.auth.server.authentification.facade.dtos.out.Bearers;
import com.example.auth.server.authentification.facade.dtos.out.UserToken;
import com.example.auth.server.authentification.facade.exceptions.*;
import com.example.auth.server.authentification.facade.services.server.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * @autor Vincent
 * @date 11/03/2020
 */

@RestController
@RequestMapping("/auth/tokens")
@AllArgsConstructor
public class TokenController {


    private TokenService tokens;


    @PostMapping(value = "/claim", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> sign(@RequestBody UserCredentials login) throws MailAlreadyTakenException, BadPasswordFormatException, InvalidMailException, ForbiddenDomainMailUseException, UserBanException {
        UserToken ut = tokens.signIn(login.getMail(), login.getPassword());

        return ResponseEntity.created(URI.create("/auth/users/" + ut.getIdUser()))
                .body(ut.getBearers());
    }


    @PostMapping(value = "/refresh", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> refresh(@RequestBody RefreshRequest request) throws NotSuchUserException, UserBanException, NoTokenException, InvalidTokenException, TokenExpiredException {

        return ResponseEntity.ok(tokens.refresh(request.getRefreshToken()));
    }

    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> log(@RequestBody UserCredentials login) throws BadPasswordException, NotSuchUserException, UserBanException {
        return ResponseEntity.ok(tokens.logIn(login.getMail(), login.getPassword()));
    }




}


