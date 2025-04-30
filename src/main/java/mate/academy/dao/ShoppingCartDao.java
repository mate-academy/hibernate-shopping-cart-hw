package mate.academy.dao;

import java.util.Optional;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

public interface ShoppingCartDao {
    void update(ShoppingCart shoppingCart);

    Optional<ShoppingCart> getByUser(User user);

    ShoppingCart add(ShoppingCart shoppingCart);
}
