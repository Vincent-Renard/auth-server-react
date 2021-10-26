package com.example.auth.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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


    private static final String rootUrl = "/auth";
    private static final String adminUrl = "/admin";
    @Autowired
    private ServerSecurityContextRepository securityContextRepository;

    @Bean
    ReactiveUserDetailsService noOps() {
        return s -> Mono.empty();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
//PUBLIC PART
                .pathMatchers(HttpMethod.GET, rootUrl + "/public").permitAll()
                .pathMatchers(HttpMethod.GET, rootUrl + "/domains").permitAll()
                .pathMatchers(HttpMethod.GET, rootUrl + "/state").permitAll()
//ADMIN PART
                .pathMatchers(HttpMethod.GET, rootUrl + adminUrl + "/state").hasAuthority("ADMIN")
                .pathMatchers(HttpMethod.GET, rootUrl + adminUrl + "/users/**").hasAuthority("ADMIN")
                .pathMatchers(HttpMethod.PATCH, rootUrl + adminUrl + "/users/*/roles").hasAuthority("ADMIN")
                .pathMatchers(HttpMethod.POST, rootUrl + adminUrl + "/domains").hasAuthority("ADMIN")
                .pathMatchers(HttpMethod.POST, rootUrl + adminUrl + "/domains/list").hasAuthority("ADMIN")

                .pathMatchers(HttpMethod.DELETE, rootUrl + adminUrl + "/domains/**").hasAuthority("ADMIN")
                .pathMatchers(HttpMethod.POST, rootUrl + adminUrl + "/users/*/ban").hasAuthority("ADMIN")
                .pathMatchers(HttpMethod.DELETE, rootUrl + adminUrl + "/users/*/ban").hasAuthority("ADMIN")
                .pathMatchers(HttpMethod.DELETE, rootUrl + adminUrl + "/clean").hasAuthority("ADMIN")
//TOKEN PART
                .pathMatchers(HttpMethod.POST, rootUrl + "/tokens/refresh").permitAll()
                .pathMatchers(HttpMethod.POST, rootUrl + "/tokens/login").permitAll()
                .pathMatchers(HttpMethod.POST, rootUrl + "/tokens/claim").permitAll()
                .pathMatchers(HttpMethod.GET, rootUrl + "/tokens/login").authenticated()

//USER PART
                .pathMatchers(HttpMethod.GET, rootUrl + "/users/password/reset/**").permitAll()
                .pathMatchers(HttpMethod.POST, rootUrl + "/users/password/reset/**").permitAll()
                .pathMatchers(HttpMethod.PATCH, rootUrl + "/users/password").authenticated()
                .pathMatchers(HttpMethod.PATCH, rootUrl + "/users/mail").authenticated()
                .pathMatchers(HttpMethod.GET, rootUrl + "/users/me").authenticated()
                .pathMatchers(HttpMethod.DELETE, rootUrl + "/users/me").authenticated()
                //.pathMatchers(HttpMethod.GET, rootUrl + "/users/**").authenticated()


                .anyExchange().denyAll();
        return http.build();
    }
}
