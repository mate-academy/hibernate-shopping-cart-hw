package mate.academy.dao;

import java.util.Optional;

public interface AbstractDao<T> {
    Optional<T> get(Long id);

    T add(T object);
}
