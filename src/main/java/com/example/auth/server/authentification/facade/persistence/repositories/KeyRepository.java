package com.example.auth.server.authentification.facade.persistence.repositories;

import com.example.auth.server.authentification.facade.persistence.entities.RSAPrivateKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @autor Vincent
 * @date 29/04/2020
 */
@Repository
public interface KeyRepository extends JpaRepository<RSAPrivateKeyEntity, Long> {

}