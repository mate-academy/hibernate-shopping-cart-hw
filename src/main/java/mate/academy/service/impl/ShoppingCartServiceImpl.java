package mate.academy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.dao.UserDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private ShoppingCartDao service;
    @Inject
    private UserDao userDao;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        final ShoppingCart shoppingCart = getByUser(user);
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        ticket = ticketDao.add(ticket);
        shoppingCart.getTickets().add(ticket);
        service.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return service.getByUser(user).orElseThrow(() ->
                new EntityNotFoundException("Can't get shopping cart by user: " + user.getId()));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        if (userDao.findByEmail(user.getEmail()).isEmpty()) {
            userDao.add(user);
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setTickets(new ArrayList<>());
        service.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(Collections.emptyList());
        service.update(shoppingCart);
    }
}
