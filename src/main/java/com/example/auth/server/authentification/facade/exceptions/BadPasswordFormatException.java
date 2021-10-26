package com.example.auth.server.authentification.facade.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @autor Vincent
 * @date 14/04/2020
 */
@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED, reason = "Bad password format")
public class BadPasswordFormatException extends Exception {
}
