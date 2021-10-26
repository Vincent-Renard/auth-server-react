package com.example.auth.server.model.repositories.logs;

import com.example.auth.server.model.entities.logs.user.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @autor Vincent
 * @date 07/09/2020
 */
@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Long> {

}
