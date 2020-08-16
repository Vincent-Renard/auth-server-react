package com.example.auth.server.authentification.facade.persistence.entities;

import com.example.auth.server.authentification.facade.persistence.entities.enums.LogUserStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @autor Vincent
 * @date 30/07/2020
 */


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@Entity
@NoArgsConstructor
public class UserLog {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated
    LogUserStatus status;

    @CreationTimestamp
    LocalDateTime date;

    public UserLog(LogUserStatus log) {
        status = log;
    }
}
