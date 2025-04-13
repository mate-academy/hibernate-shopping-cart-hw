package mate.academy.service;

import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

public interface ShoppingCartService {
    /**
     * Цей метод відповідає за додавання квитка в кошик покупок.
     * @param movieSession — сесія фільму, яка містить усю необхідну інформацію для квитка
     * @param user — користувач, який хоче купити квиток на певну сесію
     */
    void addSession(MovieSession movieSession, User user);

    ShoppingCart getByUser(User user);

    void registerNewShoppingCart(User user);

    void clear(ShoppingCart shoppingCart);
}
