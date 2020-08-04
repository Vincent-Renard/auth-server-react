package com.example.auth.server.controller;

import com.example.auth.server.authentification.facade.AuthService;
import com.example.auth.server.model.dtos.in.UpdateMailRequest;
import com.example.auth.server.model.dtos.in.UpdatePasswordRequest;
import com.example.auth.server.model.dtos.out.Bearers;
import com.example.auth.server.model.dtos.out.User;
import com.example.auth.server.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @autor Vincent
 * @date 16/07/2020
 */

@RestController
@RequestMapping(value = "/auth/users", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
public class UserController {

    @Autowired
    private AuthService base;


    @PatchMapping(value = "/password", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updatePassword(Principal user, @RequestBody UpdatePasswordRequest updatePasswordRequest) throws NotSuchUserException, BadPasswordFormat, UserBan {
        base.updatePassword(Long.parseLong(user.getName()), updatePasswordRequest.getNewPassword());
        return ResponseEntity.ok().build();

    }

    @PatchMapping(value = "/mail", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updateMail(Principal user, @RequestBody UpdateMailRequest mailRequest) throws NotSuchUserException, InvalidMail, MailAlreadyTakenException, ForbidenDomainMailUse, UserBan {
        base.updateMail(Long.parseLong(user.getName()), mailRequest.getNewmail());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/me", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> showUser(Principal user) throws NotSuchUserException {
        return ResponseEntity.ok(User.from(base.showUser(Long.parseLong(user.getName()))));
    }


    @DeleteMapping(value = "/me", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Bearers> erase(Principal user) throws NotSuchUserException, UserBan {
        base.signOut(Long.parseLong(user.getName()));
        return ResponseEntity.noContent().build();
    }

}
