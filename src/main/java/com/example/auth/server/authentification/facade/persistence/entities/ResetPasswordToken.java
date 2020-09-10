package com.example.auth.server.authentification.facade.persistence.entities;

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
 * @date 09/09/2020
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResetPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    String resetToken;


    @Setter
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Credentials user;

    @CreationTimestamp
    LocalDateTime dateTime;

    @Setter
    @Column(nullable = true)
    LocalDateTime dateTimeUse;


    public ResetPasswordToken(String token, Credentials user) {
        resetToken = token;
        this.user = user;
    }

    public void use() {
        this.dateTimeUse = LocalDateTime.now();
    }

    @PreRemove
    private void predel() {
        this.setUser(null);
    }

    @Override
    public String toString() {
        return "ResetPasswordToken{" +
                "resetToken='" + resetToken +
                ", user=" + ((user == null) ? "null" : user.getIdUser()) +
                ", dateTime=" + dateTime +
                ", dateTimeUse=" + dateTimeUse +
                '}';
    }
}
