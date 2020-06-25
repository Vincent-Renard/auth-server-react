package com.example.auth.server.authentification.facade.persistence.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @autor Vincent
 * @date 25/06/2020
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "forbiden_domains")
@ToString
public class ForbidenDomainEntity {

    @Id
    @Column(length = 512, unique = true, nullable = false)
    String domain;

    LocalDateTime dateTimeInserted;

    public ForbidenDomainEntity(String dom) {
        this.domain = dom;
        this.dateTimeInserted = LocalDateTime.now();
    }

    public ForbidenDomainEntity() {
    }
}
