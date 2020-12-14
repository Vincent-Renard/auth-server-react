package com.example.auth.server.authentification.facade.persistence.repositories;

import com.example.auth.server.authentification.facade.persistence.entities.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @autor Vincent
 * @date 09/09/2020
 */
@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, String> {

    boolean existsByResetToken(String token);

    List<ResetPasswordToken> findAllByUserIdUser(long iduser);
}
