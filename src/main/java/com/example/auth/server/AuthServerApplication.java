package com.example.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class AuthServerApplication {


    public static void main(String ... args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

}
