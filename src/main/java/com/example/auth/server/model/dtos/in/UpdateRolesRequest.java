package com.example.auth.server.model.dtos.in;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * @autor Vincent
 * @date 30/04/2020
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public class UpdateRolesRequest {
    Set<String> roles;
}
