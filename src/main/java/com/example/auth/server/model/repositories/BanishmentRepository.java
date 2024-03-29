package com.example.auth.server.model.repositories;

import com.example.auth.server.model.entities.Banishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @autor Vincent
 * @date 06/09/2020
 */
@Repository
public interface BanishmentRepository extends JpaRepository<Banishment, Long> {

	List<Banishment> findBanishmentByAdmin_IdUser(long idAdmin);
}
