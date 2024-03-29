package com.example.auth.server.authentification.facade.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @autor Vincent
 * @date 22/03/2020
 */
@ResponseStatus(value = HttpStatus.NON_AUTHORITATIVE_INFORMATION, reason = "No token in Autorization")
public class NoTokenException extends Exception {
}
