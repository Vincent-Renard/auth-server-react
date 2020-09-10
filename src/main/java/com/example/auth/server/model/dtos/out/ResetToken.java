package com.example.auth.server.model.dtos.out;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * @autor Vincent
 * @date 10/09/2020
 */

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetToken {

    String token;

    public static ResetToken from(String token) {
        ResetToken r = new ResetToken();
        r.setToken(token);
        return r;
    }
}
