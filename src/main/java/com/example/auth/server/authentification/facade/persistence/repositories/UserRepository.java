package com.example.auth.server.authentification.facade.persistence.repositories;

import com.example.auth.server.authentification.facade.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @autor Vincent
 * @date 19/04/2020
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByMail(String mail);

    boolean existsByMail(String mail);

}
