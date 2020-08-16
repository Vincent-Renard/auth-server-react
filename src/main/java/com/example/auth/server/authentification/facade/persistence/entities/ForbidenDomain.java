package com.example.auth.server.authentification.facade.persistence.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @autor Vincent
 * @date 25/06/2020
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
@ToString
public class ForbidenDomain {

    @Id
    @Column(length = 512, unique = true, nullable = false)
    String domain;

    @CreationTimestamp
    LocalDateTime dateTimeInserted;


    public ForbidenDomain(String dom) {
        this.domain = dom;
    }

    public ForbidenDomain() {
    }
}
