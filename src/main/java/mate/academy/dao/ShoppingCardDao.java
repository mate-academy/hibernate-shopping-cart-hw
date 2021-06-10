package mate.academy.dao;

import mate.academy.model.ShoppingCard;
import mate.academy.model.User;

public interface ShoppingCardDao {
    ShoppingCard add(ShoppingCard shoppingCard);

    ShoppingCard getByUser(User user);

    void update(ShoppingCard shoppingCard);
}
