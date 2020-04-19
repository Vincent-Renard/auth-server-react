package com.example.auth.server.authentification.facade;

/**
 * @autor Vincent
 * @date 31/03/2020
 */
public interface AuthUtils {
    int MIN_LENGHT_PASSWORD = 2;
    int MAX_LENGHT_PASSWORD = 64;
    int MIN_LENGHT_USERNAME = 2;
    int MAX_LENGHT_USERNAME = 64;
    String REGEX_USERNAME = "";
    String REGEX_PASSWORD = "";
}
