package mate.academy.service.impl;

import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketsDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

import java.util.Collections;
import java.util.NoSuchElementException;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    ShoppingCartDao shoppingCartDao;
    @Inject
    TicketsDao ticketsDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = ticketsDao.add(new Ticket(movieSession, user));
        ShoppingCart shoppingCart = getByUser(user);
        shoppingCart.getTickets().add(ticket);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(()->
                new NoSuchElementException("Can't get ShoppingCart by user: " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(Collections.emptyList());
        shoppingCartDao.update(shoppingCart);
    }
}
