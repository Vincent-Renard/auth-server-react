package com.example.auth.server.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @autor Vincent
 * @date 02/09/2020
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unknown Admin")
public class NotSuchAdminException extends Exception {
}
