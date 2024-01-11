package mate.academy.service;

import mate.academy.exception.EntityNotFoundException;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

public interface ShoppingCartService {
    void addSession(MovieSession movieSession, User user) throws EntityNotFoundException;

    ShoppingCart getByUser(User user) throws EntityNotFoundException;

    void registerNewShoppingCart(User user);

    void clear(ShoppingCart shoppingCart);
}
