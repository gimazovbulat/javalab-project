package ru.itis.dao.interfaces;

import java.util.Optional;

public interface CrudRepository<ID, T> {
    void save(T t);
    Optional<T> find(ID id);
    void update(T t);
    void delete(ID id);
}
