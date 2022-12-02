package mate.academy.service;

public interface AbstractService<T> {
    T add(T object);

    T get(Long id);
}
