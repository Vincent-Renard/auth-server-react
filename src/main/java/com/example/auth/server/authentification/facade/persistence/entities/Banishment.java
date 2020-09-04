package com.example.auth.server.authentification.facade.persistence.entities;

import com.example.auth.server.authentification.facade.persistence.entities.enums.BanReason;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @autor Vincent
 * @date 26/06/2020
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
public class Banishment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated
    BanReason reason;

    @CreationTimestamp
    LocalDateTime date;


    @Setter
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    @JsonIdentityReference(alwaysAsId = true)
    Credentials admin;


    public Banishment(Credentials admin, BanReason reason) {
        this.reason = reason;
        //this.admin=admin;
    }

    @Override
    public String toString() {
        return "Banishment{" +
                "id=" + id +
                ", reason=" + reason +
                ", date=" + date +
                '}';
    }
}
