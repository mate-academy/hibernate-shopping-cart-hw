package mate.academy.dao;

import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

import java.util.Optional;

@Dao
public interface ShoppingCartDao {
    ShoppingCart add(ShoppingCart shoppingCart);

    Optional<ShoppingCart> getByUser(User user);

    void update(ShoppingCart shoppingCart);
}
