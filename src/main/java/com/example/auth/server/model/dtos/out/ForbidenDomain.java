package com.example.auth.server.model.dtos.out;

import com.example.auth.server.authentification.facade.persistence.entities.ForbidenDomainEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * @autor Vincent
 * @date 25/06/2020
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Getter
@Setter
public class ForbidenDomain {

    String domain;
    LocalDateTime dateInsert;


    public static ForbidenDomain from(ForbidenDomainEntity d) {
        return new ForbidenDomain(d.getDomain(), d.getDateTimeInserted());
    }
}
