package com.example.auth.server.model.repositories.logs;

import com.example.auth.server.model.entities.logs.admin.AdminUnbanLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @autor Vincent
 * @date 09/09/2020
 */
@Repository
public interface AdminUnBanLogRepository extends JpaRepository<AdminUnbanLog, Long> {
    List<AdminUnbanLog> findAllByUserIdUser(long idUser);
}
