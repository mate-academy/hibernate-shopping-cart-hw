package mate.academy.dao;

import mate.academy.model.*;

import java.util.*;

public interface ShoppingCartDao {
    ShoppingCart add(ShoppingCart shoppingCart);

    Optional<ShoppingCart> getByUser(User user);

    void update(ShoppingCart shoppingCart);
}
