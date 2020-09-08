package com.example.auth.server.authentification.facade.persistence.repositories;

import com.example.auth.server.authentification.facade.persistence.entities.logs.user.BanUserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @autor Vincent
 * @date 08/09/2020
 */
@Repository
public interface BanLogRepository extends JpaRepository<BanUserLog, Long> {
    List<BanUserLog> findAllByAdmin_IdUser(Long idAdmin);
}
