package com.example.auth.server.controller;

import com.example.auth.server.authentification.facade.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @autor Vincent
 * @date 26/06/2020
 */

@RestController
@RequestMapping("/auth")
public class BanController {

    @Autowired
    private AuthService base;




}
