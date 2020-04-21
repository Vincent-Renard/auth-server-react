package com.example.auth.server.controller;

import com.example.auth.server.authentification.facade.AuthService;
import com.example.auth.server.authentification.facade.AuthServiceImpl;
import com.example.auth.server.model.dtos.in.UpdateMailRequest;
import com.example.auth.server.model.dtos.in.UpdatePasswordRequest;
import com.example.auth.server.model.dtos.in.UserCredentials;
import com.example.auth.server.model.dtos.out.Bearers;
import com.example.auth.server.model.dtos.out.PubKey;
import com.example.auth.server.model.dtos.out.User;
import com.example.auth.server.model.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * @autor Vincent
 * @date 11/03/2020
 */

@RestController
@RequestMapping("/auth")
public class TokenController {

    private final AuthService base = new AuthServiceImpl();

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
    public ResponseEntity<Bearers> sign(@RequestBody UserCredentials login) throws MailAlreadyTakenException, BadPasswordFormat, InvalidMail {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(base.signIn(login.getMail(), login.getPassword()));

    }

    @PatchMapping(value = "/claim/{id}/roles", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> updateRoles(@PathVariable(value = "id") long idUser, @RequestBody List<String> roles) throws NotSuchUserException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(User.from(base.updateRoles(idUser, roles)));

    }

    @PatchMapping(value = "/login/password", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updatePassword(Principal user, @RequestBody UpdatePasswordRequest updatePasswordRequest) throws NotSuchUserException, BadPasswordException, BadPasswordFormat {
        base.updatePassword(Long.parseLong(user.getName()), updatePasswordRequest.getOldPassword(), updatePasswordRequest.getNewPassword());
        return ResponseEntity.ok().build();

    }
    @PatchMapping(value = "/login/mail", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updateMail(Principal user, @RequestBody UpdateMailRequest mailRequest) throws NotSuchUserException, BadPasswordException, InvalidMail, MailAlreadyTakenException {
        base.updateMail(Long.parseLong(user.getName()), mailRequest.getPassword(), mailRequest.getNewmail());
        return ResponseEntity.ok().build();
    }
    
    @GetMapping(value = "/refresh", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> refresh(Principal user) throws NotSuchUserException {
        return ResponseEntity.ok(base.refresh(Long.parseLong(user.getName())));
    }

    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> log(@RequestBody UserCredentials login) throws BadPasswordException, NotSuchUserException {
        return ResponseEntity.ok(base.logIn(login.getMail(), login.getPassword()));
    }

    @DeleteMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> erase(Principal user,@RequestBody String password) throws BadPasswordException, NotSuchUserException {
        base.signOut(Long.parseLong(user.getName()), password);
        return ResponseEntity.noContent().build();
    }
}

