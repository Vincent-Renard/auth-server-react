package com.example.auth.server.model.repositories.logs;

import com.example.auth.server.model.entities.logs.user.RoleUpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @autor Vincent
 * @date 07/09/2020
 */
@Repository
public interface RoleUpdateLogRepository extends JpaRepository<RoleUpdateLog, Long> {

	List<RoleUpdateLog> findAllByAdmin_IdUser(long idAdmin);
}
