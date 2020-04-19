package com.example.auth.server.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @autor Vincent
 * @date 12/03/2020
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Unknown User")
public class NotSuchUserException extends Exception {
}
