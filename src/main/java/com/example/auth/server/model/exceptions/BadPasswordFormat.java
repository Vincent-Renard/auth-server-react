package com.example.auth.server.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @autor Vincent
 * @date 14/04/2020
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Bad password format")
public class BadPasswordFormat extends Throwable {
}
