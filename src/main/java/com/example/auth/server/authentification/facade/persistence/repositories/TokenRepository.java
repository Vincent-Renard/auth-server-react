package com.example.auth.server.authentification.facade.persistence.repositories;

import com.example.auth.server.authentification.facade.persistence.entities.TokensId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @autor Vincent
 * @date 22/07/2020
 */
@Repository
public interface TokenRepository extends JpaRepository<TokensId, Long> {
}
