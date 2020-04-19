package com.example.auth.server.authentification.facade.persistence;

import com.example.auth.server.model.StoreUser;
import com.example.auth.server.model.exceptions.ValueCreatingError;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.lang.Long;
/**
 * @autor Vincent
 * @date 15/04/2020
 */

public class RepositoryImpl{
    private final AtomicLong ids = new AtomicLong(1L);
    private final Map<Long, StoreUser> repo = new HashMap<>();

    public Optional<StoreUser> findById(long id) {
        return Optional.ofNullable(repo.get(id));
    }

    public void deleteById(long id) {
        repo.remove(id);
    }

    public long store(StoreUser value) throws ValueCreatingError {
        if (repo.values().stream().anyMatch(u-> u.getMail().equals(value.getMail()))){
            throw new ValueCreatingError();
        }
        long id = ids.getAndIncrement();
        value.setIdUser(id);
        repo.put(id, value);
        return id;
    }


    public StoreUser updateById(long id, StoreUser value) {
        repo.put(id, value);
        return repo.get(id);
    }


    public void clear() {
        repo.clear();
    }

    public Optional<StoreUser> findByMail(String mail) {

        return repo.values().stream().filter(user -> user.getMail().equals(mail)).findFirst();
    }
}
