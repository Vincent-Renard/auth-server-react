package com.example.auth.server.authentification.facade.persistence.entities.logs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;

/**
 * @autor Vincent
 * @date 16/08/2020
 */

@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@Entity
public class BanLog extends UserLog {
}
