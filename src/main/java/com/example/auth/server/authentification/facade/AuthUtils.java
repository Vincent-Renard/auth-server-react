package com.example.auth.server.authentification.facade;

import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @autor Vincent
 * @date 31/03/2020
 */
public interface AuthUtils {
    int TTL_SECONDS_RESET_PASSWORD_TOKEN = 600;
    int MIN_LENGHT_PASSWORD = 2;
    int MAX_LENGHT_PASSWORD = 64;
    String REGEX_PASSWORD = "";
    String REGEX_MAIL = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    Predicate<String> passwordChecker = password -> password.length() > MIN_LENGHT_PASSWORD && password.length() < MAX_LENGHT_PASSWORD;
    Predicate<String> mailChecker = Pattern.compile(REGEX_MAIL, Pattern.CASE_INSENSITIVE).asPredicate();

    Set<String> BASE_ROLES = Set.of("USER");
    Set<String> POSSILBES_ROLES = Set.of("ADMIN", "USER");
    String mailAdmin = "admin@admin.com";


}
