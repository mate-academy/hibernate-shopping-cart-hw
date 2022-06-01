package mate.academy.service;

import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCard;
import mate.academy.model.User;

public interface ShoppingCardService {
    void addSession(MovieSession movieSession, User user);

    ShoppingCard getByUser(User user);

    void registerNewShoppingCard(User user);

    void clear(ShoppingCard shoppingCard);
}
