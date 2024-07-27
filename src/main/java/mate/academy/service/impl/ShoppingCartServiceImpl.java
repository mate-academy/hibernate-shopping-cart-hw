package mate.academy.service.impl;

import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Override
    public void addSession(MovieSession movieSession, User user) {

    }

    @Override
    public ShoppingCart getByUser(User user) {
        return null;
    }

    @Override
    public void registerNewShoppingCart(User user) {

    }

    @Override
    public void clear(ShoppingCart shoppingCart) {

    }
}

