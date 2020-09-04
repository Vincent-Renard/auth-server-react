package com.example.auth.server.authentification.facade.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @autor Vincent
 * @date 25/06/2020
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @Setter
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonIdentityReference(alwaysAsId = true)
    Credentials admin;

    public ForbidenDomain(Credentials admin, String dom) {
        //this.admin=admin;
        this.domain = dom;
    }


}
