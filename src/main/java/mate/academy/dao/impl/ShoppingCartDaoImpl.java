package mate.academy.dao.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    private static final Map<Long, ShoppingCart> storage = new HashMap<>();
    private static long idCounter = 1;

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        shoppingCart.setId(idCounter++);
        storage.put(shoppingCart.getUser().getId(), shoppingCart);
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        return Optional.ofNullable(storage.get(user.getId()));
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        storage.put(shoppingCart.getUser().getId(), shoppingCart);
    }
}
