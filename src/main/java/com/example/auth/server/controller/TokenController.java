package com.example.auth.server.controller;

import com.example.auth.server.authentification.facade.AuthService;
import com.example.auth.server.authentification.facade.pojos.UserToken;
import com.example.auth.server.model.dtos.in.RefreshRequest;
import com.example.auth.server.model.dtos.in.UserCredentials;
import com.example.auth.server.model.dtos.out.Bearers;
import com.example.auth.server.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TokenController {

    @Autowired
    private AuthService base;


    @PostMapping(value = "/claim", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> sign(@RequestBody UserCredentials login) throws MailAlreadyTakenException, BadPasswordFormat, InvalidMail, ForbidenDomainMailUse, UserBan {
        UserToken ut = base.signIn(login.getMail(), login.getPassword());

        return ResponseEntity.created(URI.create("/auth/users/" + ut.getIdUser()))
                .body(ut.getBearers());
    }


    @PostMapping(value = "/refresh", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> refresh(@RequestBody RefreshRequest request) throws NotSuchUserException, UserBan, NoToken, InvalidToken, TokenExpired {

        return ResponseEntity.ok(base.refresh(request.getRefreshToken()));
    }

    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> log(@RequestBody UserCredentials login) throws BadPasswordException, NotSuchUserException, UserBan {
        return ResponseEntity.ok(base.logIn(login.getMail(), login.getPassword()));
    }




}


