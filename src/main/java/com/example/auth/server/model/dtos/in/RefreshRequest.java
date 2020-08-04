package com.example.auth.server.model.dtos.in;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @autor Vincent
 * @date 04/08/2020
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshRequest {
    String refreshToken;
}
