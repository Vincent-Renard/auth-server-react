package com.example.auth.server.controller;

import com.example.auth.server.authentification.facade.AuthService;
import com.example.auth.server.model.dtos.in.UserCredentials;
import com.example.auth.server.model.dtos.out.Bearers;
import com.example.auth.server.model.dtos.out.User;
import com.example.auth.server.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @autor Vincent
 * @date 11/03/2020
 */

@RestController
@RequestMapping("/auth")
public class TokenController {

    @Autowired
    private AuthService base;



    @PostMapping(value = "/claim", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> sign(@RequestBody UserCredentials login) throws MailAlreadyTakenException, BadPasswordFormat, InvalidMail, ForbidenDomainMailUse, UserBan {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(base.signIn(login.getMail(), login.getPassword()));

    }




    @GetMapping(value = "/refresh", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> refresh(Principal user) throws NotSuchUserException, UserBan {
        return ResponseEntity.ok(base.refresh(Long.parseLong(user.getName())));
    }

    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> log(@RequestBody UserCredentials login) throws BadPasswordException, NotSuchUserException, UserBan {
        return ResponseEntity.ok(base.logIn(login.getMail(), login.getPassword()));
    }


    @GetMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> showMe(Principal principal) throws NotSuchUserException {
        return ResponseEntity.ok(User.from(base.showUser(Long.parseLong(principal.getName()))));
    }


}


