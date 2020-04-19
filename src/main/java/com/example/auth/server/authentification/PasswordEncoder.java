package com.example.auth.server.authentification;

/**
 * @autor Vincent
 * @date 12/03/2020
 */

public class PasswordEncoder {

    public static char[] encode(String clearPassword) {
        return clearPassword.toCharArray();
    }

    public static PasswordEncoder getEncoder() {
        return new PasswordEncoder();
    }
}
