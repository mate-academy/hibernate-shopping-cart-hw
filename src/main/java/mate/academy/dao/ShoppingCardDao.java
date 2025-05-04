package mate.academy.dao;

import java.util.Optional;
import mate.academy.model.ShoppingCard;
import mate.academy.model.User;

public interface ShoppingCardDao {
    ShoppingCard add(ShoppingCard shoppingCard);

    Optional<ShoppingCard> getByUser(User user);

    void update(ShoppingCard shoppingCard);
}
