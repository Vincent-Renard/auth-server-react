package com.example.auth.server.authentification.facade.persistence;

import com.example.auth.server.model.StoreUser;
import com.example.auth.server.model.exceptions.ValueCreatingError;

import java.util.Optional;

/**
 * @autor Vincent
 * @date 19/04/2020
 */
public interface Repository {
    Optional<StoreUser> findById(long id);

    void deleteById(long id);

    long store(StoreUser value) throws ValueCreatingError;

    StoreUser update(StoreUser value);

    void clear();

    Optional<StoreUser> findByMail(String mail);
}
