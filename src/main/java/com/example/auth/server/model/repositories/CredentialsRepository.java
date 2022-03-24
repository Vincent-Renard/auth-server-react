package com.example.auth.server.model.repositories;

import com.example.auth.server.model.entities.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @autor Vincent
 * @date 19/04/2020
 */
@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {
	Optional<Credentials> findByMail(String mail);

	boolean existsByMail(String mail);

	long count();


}
