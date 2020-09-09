package com.example.auth.server.authentification.facade.persistence.entities;

import com.example.auth.server.authentification.facade.persistence.entities.logs.user.UserLog;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

/**
 * @autor Vincent
 * @date 12/03/2020
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idUser")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
//@ToString TODO --//
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Setter
    @OneToOne(cascade = CascadeType.ALL, optional = true, mappedBy = "user")
    private Banishment banishment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @Setter
    @JsonManagedReference
    List<UserLog> logs = new ArrayList<>();


    public Credentials(String mail, String password, Collection<String> roles) {

        this.mail = mail.toLowerCase();
        this.password = password;
        this.roles = roles;

    }

    public void addLog(UserLog log) {
        log.setUser(this);
        this.getLogs().add(log);
    }


    public String toString() {
        return "Credentials{" +
                "idUser=" + idUser +
                ", mail='" + mail + '\'' +
                ", inscriptionDate=" + inscriptionDate +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", updateDate=" + updateDate +
                ", banishment=" + ((banishment == null) ? "null" : banishment.toString()) +
                ", logs=" + logs +
                '}';
    }



}
