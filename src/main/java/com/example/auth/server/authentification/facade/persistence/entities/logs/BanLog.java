package com.example.auth.server.authentification.facade.persistence.entities.logs;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

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

    @CreationTimestamp
    LocalDateTime updateDate;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    Credentials user;

    @Setter
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    Credentials admin;


}
