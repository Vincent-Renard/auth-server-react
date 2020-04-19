package com.example.auth.server.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @autor Vincent
 * @date 18/04/2020
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Mail invalid")
public class InvalidMail extends Throwable {
}
