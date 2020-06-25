package com.example.auth.server.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @autor Vincent
 * @date 25/06/2020
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Domain in use")
public class DomainTakenByUser extends Exception {
}
