package com.example.auth.server.model.repositories.logs;

import com.example.auth.server.model.entities.logs.user.BanLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @autor Vincent
 * @date 08/09/2020
 */
@Repository
public interface BanLogRepository extends JpaRepository<BanLog, Long> {
	List<BanLog> findAllByAdmin_IdUser(Long idAdmin);
}
