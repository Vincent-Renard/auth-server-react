package com.example.auth.server.authentification.facade.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @autor Vincent
 * @date 18/04/2020
 */
@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED, reason = "Mail invalid")
public class InvalidMailException extends Exception {
}
