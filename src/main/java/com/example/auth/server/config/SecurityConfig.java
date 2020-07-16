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

                .pathMatchers(HttpMethod.GET, rootUrl + "/public").permitAll()
                .pathMatchers(HttpMethod.POST, rootUrl + "/login").permitAll()
                //.pathMatchers(HttpMethod.GET, "**/swagger**").permitAll()
                .pathMatchers(HttpMethod.PATCH, rootUrl + "/login/password").authenticated()
                .pathMatchers(HttpMethod.GET, rootUrl + "/login").authenticated()
                .pathMatchers(HttpMethod.GET, rootUrl + "/login/**").hasAuthority("ADMIN")
                .pathMatchers(HttpMethod.DELETE, rootUrl + "/login").authenticated()
                .pathMatchers(HttpMethod.PATCH, rootUrl + "/login/mail").authenticated()

                .pathMatchers(HttpMethod.POST, rootUrl + "/claim").permitAll()
                .pathMatchers(HttpMethod.PATCH, rootUrl + "/login/**/roles").hasAuthority("ADMIN")
                .pathMatchers(HttpMethod.POST, rootUrl + "/domains").hasAuthority("ADMIN")
                .pathMatchers(HttpMethod.DELETE, rootUrl + "/domains").hasAuthority("ADMIN")
                .pathMatchers(HttpMethod.POST, rootUrl + "/login/**/ban").hasAuthority("ADMIN")
                .pathMatchers(HttpMethod.DELETE, rootUrl + "/login/**/ban").hasAuthority("ADMIN")
                .pathMatchers(HttpMethod.GET, rootUrl + "/domains").permitAll()
                .pathMatchers(HttpMethod.GET, rootUrl + "/refresh").authenticated()
                .pathMatchers(HttpMethod.DELETE, rootUrl + "/clean").hasAuthority("ADMIN")
                .anyExchange().denyAll();
        return http.build();
    }
}
