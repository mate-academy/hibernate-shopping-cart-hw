package mate.academy.service.impl;

import mate.academy.dao.ShoppingCartDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    ShoppingCartDao shoppingCartDao;

    @Override

    public void addSession(MovieSession movieSession, User user) {

    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(() ->
                new NoSuchElementException("ShoppingCart do not exist"));
    }

    @Override
    public void registerNewShoppingCart(User user) {

    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTicket(List.of());
        shoppingCartDao.update(shoppingCart);
    }
}
