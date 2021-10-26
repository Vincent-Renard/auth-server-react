package com.example.auth.server.controller;

import com.example.auth.server.authentification.facade.dtos.in.UpdateMailRequest;
import com.example.auth.server.authentification.facade.dtos.in.UpdatePasswordRequest;
import com.example.auth.server.authentification.facade.dtos.out.ResetToken;
import com.example.auth.server.authentification.facade.dtos.out.User;
import com.example.auth.server.authentification.facade.exceptions.*;
import com.example.auth.server.authentification.facade.services.users.UserService;
import com.example.auth.server.authentification.facade.services.users.password.PasswordService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class UserController {


    private UserService users;
    private PasswordService passwordService;

    @GetMapping(value = "/password/reset", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResetToken> resetPassword(@RequestParam("mail") String mail) throws NotSuchUserException, UserBanException {
        return ResponseEntity.ok(ResetToken.from(passwordService.askResetPasswordToken(mail).getResetToken()));

    }

    @PostMapping(value = "/password/reset", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> resetUpdatePassword(@RequestParam("key") String key, @RequestBody UpdatePasswordRequest upr) throws NotSuchUserException, UserBanException, BadPasswordFormatException, TokenNotFoundException {

        passwordService.useResetPasswordToken(key, upr.getNewPassword());
        return ResponseEntity.ok().build();

    }

    @PatchMapping(value = "/password", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updatePassword(Principal user, @RequestBody UpdatePasswordRequest updatePasswordRequest) throws NotSuchUserException, BadPasswordFormatException, UserBanException {
        passwordService.updatePassword(Long.parseLong(user.getName()), updatePasswordRequest.getNewPassword());
        return ResponseEntity.ok().build();

    }

    @PatchMapping(value = "/mail", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updateMail(Principal user, @RequestBody UpdateMailRequest mailRequest) throws NotSuchUserException, InvalidMailException, MailAlreadyTakenException, ForbiddenDomainMailUseException, UserBanException {
        users.updateMail(Long.parseLong(user.getName()), mailRequest.getNewmail());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/me", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> showUser(Principal user) throws NotSuchUserException {
        return ResponseEntity.ok(User.from(users.showUser(Long.parseLong(user.getName()))));
    }


    @DeleteMapping(value = "/me", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> erase(Principal user) throws NotSuchUserException, UserBanException {
        users.signOut(Long.parseLong(user.getName()));
        return ResponseEntity.noContent().build();
    }

}
