package com.example.auth.server.controller;

import com.example.auth.server.authentification.facade.AuthService;
import com.example.auth.server.model.dtos.in.*;
import com.example.auth.server.model.dtos.out.Bearers;
import com.example.auth.server.model.dtos.out.PubKey;
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

    @GetMapping(value = "/public", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PubKey> pubkey() {
        return ResponseEntity.ok(PubKey.from(base.publicKey().getEncoded()));
    }

    @DeleteMapping(value = "/clean")
    public ResponseEntity<Void> clear() {
        base.clear();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PostMapping(value = "/claim", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> sign(@RequestBody UserCredentials login) throws MailAlreadyTakenException, BadPasswordFormat, InvalidMail, ForbidenDomainMailUse, UserBan {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(base.signIn(login.getMail(), login.getPassword()));

    }

    @PatchMapping(value = "/login/{id}/roles", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> updateRoles(@PathVariable(value = "id") long idUser, @RequestBody UpdateRolesRequest updateRolesRequest) throws NotSuchUserException {
        return ResponseEntity
                .ok(User.from(base.updateRoles(idUser, updateRolesRequest.getRoles())));

    }

    @PatchMapping(value = "/login/password", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updatePassword(Principal user, @RequestBody UpdatePasswordRequest updatePasswordRequest) throws NotSuchUserException, BadPasswordException, BadPasswordFormat, UserBan {
        base.updatePassword(Long.parseLong(user.getName()), updatePasswordRequest.getOldPassword(), updatePasswordRequest.getNewPassword());
        return ResponseEntity.ok().build();

    }

    @PatchMapping(value = "/login/mail", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updateMail(Principal user, @RequestBody UpdateMailRequest mailRequest) throws NotSuchUserException, BadPasswordException, InvalidMail, MailAlreadyTakenException, ForbidenDomainMailUse, UserBan {
        base.updateMail(Long.parseLong(user.getName()), mailRequest.getPassword(), mailRequest.getNewmail());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/refresh", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> refresh(Principal user) throws NotSuchUserException, UserBan {
        return ResponseEntity.ok(base.refresh(Long.parseLong(user.getName())));
    }

    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> log(@RequestBody UserCredentials login) throws BadPasswordException, NotSuchUserException, UserBan {
        return ResponseEntity.ok(base.logIn(login.getMail(), login.getPassword()));
    }

    @DeleteMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> erase(Principal user, @RequestBody DeleteCredentialsRequest deleteCredentialsRequest) throws BadPasswordException, NotSuchUserException, UserBan {
        base.signOut(Long.parseLong(user.getName()), deleteCredentialsRequest.getPassword());
        return ResponseEntity.noContent().build();
    }
}


