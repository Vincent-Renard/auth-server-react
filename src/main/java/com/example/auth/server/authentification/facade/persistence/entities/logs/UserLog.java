package com.example.auth.server.authentification.facade.persistence.entities.logs;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.enums.LogUserStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
 * @date 30/07/2020
 */


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
@NoArgsConstructor
@Inheritance
public class UserLog {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated
    @Setter
    LogUserStatus status;

    @CreationTimestamp
    LocalDateTime date;

    @ManyToOne
    @Setter

    @JsonBackReference
    Credentials user;


    public UserLog(LogUserStatus log) {
        status = log;
    }

    @Override
    public String toString() {
        return "UserLog{" +
                "id=" + id +
                ", status=" + status +
                ", date=" + date +
                ", iduser=" + user.getIdUser() +
                '}';
    }
}
