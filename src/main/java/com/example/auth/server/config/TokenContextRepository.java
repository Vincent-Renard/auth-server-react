package com.example.auth.server.config;

import com.example.auth.server.authentification.token.manager.JwtDecoder;
import com.example.auth.server.model.exceptions.InvalidTokenException;
import com.example.auth.server.model.exceptions.NoTokenException;
import com.example.auth.server.model.exceptions.TokenExpiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @autor Vincent
 * @date 09/04/2020
 */
@Component
public class TokenContextRepository implements ServerSecurityContextRepository {

    private static final Logger logger = LoggerFactory.getLogger(TokenContextRepository.class);
    @Autowired
    JwtDecoder bearerDecoder;

    @Override
    public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
        return null;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        try {
            var upat = bearerDecoder.decode(authorization);
            return Mono.just(new SecurityContextImpl(upat));
        } catch (NoTokenException noTokenException) {
            //logger.info("noTokenException");
        } catch (InvalidTokenException invalidTokenException) {
            //logger.info("invalidTokenException");
        } catch (TokenExpiredException tokenExpiredException) {
            //logger.info("TokenExpiredException");
        }
        return Mono.empty();

    }

}
