package com.example.auth.server.authentification.facade.persistence.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.TreeSet;

/**
 * @autor Vincent
 * @date 12/03/2020
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@Entity
public class Credentials {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idUser;

    @Column(unique = true, nullable = false)
    @Setter
    @Email
    String mail;

    @CreationTimestamp
    LocalDateTime inscriptionDate;


    @Column(nullable = false)
    @Setter
    String password;


    @Setter
    @ElementCollection
    Collection<String> roles = new TreeSet<>();

    @UpdateTimestamp
    LocalDateTime updateDate;


    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @Setter
    private Banishment banishment;

    protected Credentials() {
    }

    public Credentials(String mail, String password, Collection<String> roles) {

        this.mail = mail.toLowerCase();
        this.password = password;
        this.roles = roles;
    }
}
