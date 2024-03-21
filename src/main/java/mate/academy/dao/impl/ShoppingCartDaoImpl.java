package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        return null;
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        return Optional.empty();
    }

    @Override
    public void update(ShoppingCart shoppingCart) {

    }
}
