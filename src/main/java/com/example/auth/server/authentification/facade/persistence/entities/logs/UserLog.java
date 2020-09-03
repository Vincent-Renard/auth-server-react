package com.example.auth.server.authentification.facade.persistence.entities.logs;

import com.example.auth.server.authentification.facade.persistence.entities.enums.LogStatus;
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

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
@Inheritance
public class UserLog {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated
    @Setter
    LogStatus status;

    @CreationTimestamp
    LocalDateTime date;


    public UserLog(LogStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserLog{" +
                "id=" + id +
                ", status=" + status +
                ", date=" + date +
                '}';
    }
}
