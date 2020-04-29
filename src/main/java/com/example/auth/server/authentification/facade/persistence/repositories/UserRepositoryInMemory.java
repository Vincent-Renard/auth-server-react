package com.example.auth.server.authentification.facade.persistence.repositories;

import com.example.auth.server.model.StoreUser;
import com.example.auth.server.model.exceptions.ValueCreatingError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @autor Vincent
 * @date 15/04/2020
 */

public class UserRepositoryInMemory implements UserRepository {
    private final AtomicLong ids = new AtomicLong(1L);
    private final Map<Long, StoreUser> repo = new HashMap<>();

    @Override
    public Optional<StoreUser> findById(long id) {
        return Optional.ofNullable(repo.get(id));
    }

    @Override
    public void deleteById(long id) {
        repo.remove(id);
    }

    @Override
    public long store(StoreUser value) throws ValueCreatingError {
        if (repo.values().stream().anyMatch(u -> u.getMail().equals(value.getMail()))) {
            throw new ValueCreatingError();
        }
        long id = ids.getAndIncrement();
        value.setIdUser(id);
        repo.put(id, value);
        return id;
    }


    @Override
    public StoreUser update(StoreUser value) {
        repo.put(value.getIdUser(), value);
        return repo.get(value.getIdUser());
    }


    @Override
    public void clear() {
        repo.clear();
    }

    @Override
    public Optional<StoreUser> findByMail(String mail) {

        return repo.values().stream().filter(user -> user.getMail().equals(mail)).findFirst();
    }
}
