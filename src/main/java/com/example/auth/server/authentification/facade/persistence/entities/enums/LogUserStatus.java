package com.example.auth.server.authentification.facade.persistence.entities.enums;

/**
 * @autor Vincent
 * @date 30/07/2020
 */
public enum LogUserStatus {
    REGISTRATION,
    REFRESHING,
    LOGING,
    BAD_PASSWORD,
    ROLES_UPDATE,
    UPDATE_PASSWORD,
    UPDATE_MAIL,
    BAN,
    UNBAN
}