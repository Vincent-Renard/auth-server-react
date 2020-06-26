package com.example.auth.server.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @autor Vincent
 * @date 26/06/2020
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User already ban")
public class UserAlreadyBanException extends Exception {
}
