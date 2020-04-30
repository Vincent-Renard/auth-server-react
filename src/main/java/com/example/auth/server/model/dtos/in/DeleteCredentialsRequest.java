package com.example.auth.server.model.dtos.in;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * @autor Vincent
 * @date 30/04/2020
 */

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteCredentialsRequest {
    String password;
}
