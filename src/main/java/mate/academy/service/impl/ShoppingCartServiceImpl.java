package mate.academy.service.impl;

import java.util.Collections;
import mate.academy.dao.MovieSessionDao;
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
    private MovieSessionDao movieSessionDao;
    @Inject
    private TicketDao ticketDao;
    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket newTicket = ticketDao.add(new Ticket(movieSession, user));
        ShoppingCart usersShoppingCart = shoppingCartDao.getByUser(user).orElseThrow(() ->
                new RuntimeException("Can't add ticket: " + newTicket
                        + ". There is no available Shopping cards at user: " + user));
        usersShoppingCart.getTickets().add(newTicket);
        shoppingCartDao.update(usersShoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(() ->
                new RuntimeException("There is no available Shopping cards at user: " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        shoppingCartDao.add(new ShoppingCart(user));
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        ShoppingCart cart = shoppingCartDao.getByUser(shoppingCart.getUser())
                .orElseThrow(() -> new RuntimeException(
                        "There is no available Shopping cards: " + shoppingCart));
        cart.setTickets(Collections.emptyList());
        shoppingCartDao.update(cart);
    }
}
