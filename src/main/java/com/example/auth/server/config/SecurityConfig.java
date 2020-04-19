package com.example.auth.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import reactor.core.publisher.Mono;

/**
 * @autor Vincent
 * @date 09/04/2020
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {


    @Autowired
    private ServerSecurityContextRepository securityContextRepository;
    private static final String rootUrl  = "/auth";


    @Bean
    ReactiveUserDetailsService noOps() {
        return s -> Mono.empty();
    }


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(HttpMethod.POST, rootUrl+"/login").permitAll()
                //.pathMatchers(HttpMethod.GET, "/swagger**").permitAll()
                .pathMatchers(HttpMethod.PATCH, rootUrl+"/login/password").authenticated()
                .pathMatchers(HttpMethod.PATCH, rootUrl+"/login/mail").authenticated()
                .pathMatchers(HttpMethod.GET, rootUrl+"/public").permitAll()
                .pathMatchers(HttpMethod.POST, rootUrl+"/claim").permitAll()
                .pathMatchers(HttpMethod.PATCH, rootUrl+"/claim/**/roles").hasAuthority("ADMIN")
                .pathMatchers(HttpMethod.GET, rootUrl+"/refresh").authenticated()
                .pathMatchers(HttpMethod.DELETE, rootUrl+"/clean").hasAuthority("ADMIN")
                .anyExchange().denyAll();
        return http.build();
    }
}
