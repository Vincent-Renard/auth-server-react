package com.example.auth.server.controller;

import com.example.auth.server.authentification.facade.AuthService;
import com.example.auth.server.model.dtos.in.BanDomainRequest;
import com.example.auth.server.model.dtos.in.BanDomainsRequest;
import com.example.auth.server.model.dtos.in.BanUserRequest;
import com.example.auth.server.model.dtos.in.UpdateRolesRequest;
import com.example.auth.server.model.dtos.out.AuthServerStateAdmin;
import com.example.auth.server.model.dtos.out.User;
import com.example.auth.server.model.exceptions.ForbidenDomainMailUse;
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
 * @date 25/06/2020
 */
@RestController
@RequestMapping("/auth/admin")
public class AdminController {
    @Autowired
    private AuthService base;


    @PostMapping(value = "/domains", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> addDomain(@RequestBody BanDomainRequest domain) {
        base.addForbidenDomain(domain.getDomain());
        return ResponseEntity.ok().build();

    }

    @PostMapping(value = "/domains/list", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> addDomains(@RequestBody BanDomainsRequest request) {

        base.addForbidenDomains(request.getDomains());
        return ResponseEntity.ok().build();

    }


    @DeleteMapping(value = "/domains", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> delDomain(@RequestBody BanDomainRequest domain) throws ForbidenDomainMailUse {
        base.delForbidenDomain(domain.getDomain());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping(value = "/state", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AuthServerStateAdmin> stateAdmin() {
        return ResponseEntity.ok(base.getServerStateAdmin());
    }

    @GetMapping(value = "/users/{iduser}", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> showUser(@PathVariable(name = "iduser") long idUser) throws NotSuchUserException {
        return ResponseEntity.ok(User.from(base.showUser(idUser)));
    }


    @PatchMapping(value = "/users/{id}/roles", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> updateRoles(@PathVariable(value = "id") long idUser, @RequestBody UpdateRolesRequest updateRolesRequest) throws NotSuchUserException {
        return ResponseEntity
                .ok(User.from(base.updateRoles(idUser, updateRolesRequest.getRoles())));

    }

    @PostMapping(value = "/users/{iduser}/ban", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> sign(Principal principal, @PathVariable(name = "iduser") long idUser, @RequestBody BanUserRequest bur) throws NotSuchUserException, UserAlreadyBanException {
        return ResponseEntity.ok(
                User.from(base.banUser(idUser,
                        bur.getReason(),
                        Long.parseLong(principal.getName()))));

    }

    @DeleteMapping(value = "/users/{iduser}/ban")
    public ResponseEntity<Void> unBan(Principal principal, @PathVariable(name = "iduser") long idUser) throws NotSuchUserException {
        base.unBanUser(idUser, Long.parseLong(principal.getName()));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/clean")
    public ResponseEntity<Void> clear() {
        base.clear();
        return ResponseEntity.noContent().build();
    }


}
