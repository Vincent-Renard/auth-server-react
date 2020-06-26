package com.example.auth.server.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @autor Vincent
 * @date 26/06/2020
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User  ban")
public class UserBan extends Exception {
}
