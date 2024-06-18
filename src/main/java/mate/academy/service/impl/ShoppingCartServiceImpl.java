package mate.academy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
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
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket newTicket = new Ticket();
        newTicket.setUser(user);
        newTicket.setMovieSession(movieSession);
        ticketDao.add(newTicket);
        ShoppingCart shoppingCartFromDB = getByUser(user);
        shoppingCartFromDB.getTickets().add(newTicket);
        shoppingCartDao.update(shoppingCartFromDB);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(() ->
                new EntityNotFoundException("Couldn't get shopping cart by user " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setUser(user);
        newShoppingCart.setTickets(Collections.emptyList());
        shoppingCartDao.add(newShoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);
    }
}
