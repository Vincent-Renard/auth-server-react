package com.example.auth.server.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @autor Vincent
 * @date 22/03/2020
 */
@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED, reason = "Expired token")
public class TokenExpiredException extends Exception {
}
