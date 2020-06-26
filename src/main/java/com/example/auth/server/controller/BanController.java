package com.example.auth.server.controller;

import com.example.auth.server.authentification.facade.AuthService;
import com.example.auth.server.model.dtos.in.BanUserRequest;
import com.example.auth.server.model.dtos.out.User;
import com.example.auth.server.model.exceptions.NotSuchUserException;
import com.example.auth.server.model.exceptions.UserAlreadyBanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @autor Vincent
 * @date 26/06/2020
 */

@RestController
@RequestMapping("/auth")
public class BanController {

    @Autowired
    private AuthService base;

    @DeleteMapping(value = "/login/{iduser}/ban")
    public ResponseEntity<Void> unBan(Principal principal, @PathVariable(name = "iduser") long idUser) throws NotSuchUserException {
        base.unBanUser(idUser, Long.parseLong(principal.getName()));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PostMapping(value = "/login/{iduser}/ban", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> sign(Principal principal, @PathVariable(name = "iduser") long idUser, @RequestBody BanUserRequest bur) throws NotSuchUserException, UserAlreadyBanException {
        return ResponseEntity.ok(
                User.from(base.banUser(idUser,
                        bur.getReason(),
                        Long.parseLong(principal.getName()))));

    }


}
