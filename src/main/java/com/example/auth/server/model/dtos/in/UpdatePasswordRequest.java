package com.example.auth.server.model.dtos.in;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * @autor Vincent
 * @date 14/04/2020
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Setter
public class UpdatePasswordRequest {
    String newPassword;
}
