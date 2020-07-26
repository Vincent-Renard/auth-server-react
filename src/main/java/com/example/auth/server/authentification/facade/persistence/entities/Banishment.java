package com.example.auth.server.authentification.facade.persistence.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @autor Vincent
 * @date 26/06/2020
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@Entity
public class Banishment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    BanReason reason;

    @CreationTimestamp
    LocalDateTime date;

    public Banishment() {
    }

    public Banishment(BanReason reason) {
        this.reason = reason;
    }
}
