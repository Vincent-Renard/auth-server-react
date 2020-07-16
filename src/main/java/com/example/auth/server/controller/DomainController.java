package com.example.auth.server.controller;

import com.example.auth.server.authentification.facade.AuthService;
import com.example.auth.server.model.dtos.in.BanDomainRequest;
import com.example.auth.server.model.dtos.out.ForbidenDomain;
import com.example.auth.server.model.exceptions.ForbidenDomainMailUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @autor Vincent
 * @date 25/06/2020
 */
@RestController
@RequestMapping("/auth")
public class DomainController {
    @Autowired
    private AuthService base;


    @PostMapping(value = "/domains", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ForbidenDomain> addDomain(@RequestBody BanDomainRequest domain) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ForbidenDomain.from(base.addForbidenDomain(domain.getDomain())));

    }

    @DeleteMapping(value = "/domains", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> delDomain(@RequestBody BanDomainRequest domain) throws ForbidenDomainMailUse {
        base.delForbidenDomain(domain.getDomain());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


}
