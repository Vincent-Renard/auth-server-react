package com.example.auth.server.authentification.facade.persistence.repositories;

import com.example.auth.server.authentification.facade.persistence.entities.ForbidenDomainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @autor Vincent
 * @date 25/06/2020
 */
public interface ForbidenDomainRepository extends JpaRepository<ForbidenDomainEntity, String> {

    boolean existsByDomain(String domain);

}
