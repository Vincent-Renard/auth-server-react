package com.example.auth.server.model.repositories;

import com.example.auth.server.model.entities.RSAKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @autor Vincent
 * @date 29/04/2020
 */
@Repository
public interface KeyRepository extends JpaRepository<RSAKey, Long> {

}