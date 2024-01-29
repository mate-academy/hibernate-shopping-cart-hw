package mate.academy.service.impl;

import java.util.Collections;
import java.util.NoSuchElementException;
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
    private TicketDao ticketDao;
    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket newTicket = new Ticket();
        newTicket.setMovieSession(movieSession);
        newTicket.setUser(user);
        ticketDao.add(newTicket);
        ShoppingCart shoppingCart = getByUser(user);
        shoppingCart.getTickets().add(newTicket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(
                () -> new NoSuchElementException(
                        "Shopping cart doesn't exist for user: " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setTickets(Collections.EMPTY_LIST);
        newShoppingCart.setUser(user);
        shoppingCartDao.add(newShoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(Collections.EMPTY_LIST);
        shoppingCartDao.update(shoppingCart);
    }
}
