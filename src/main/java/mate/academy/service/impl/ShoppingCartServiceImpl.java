package mate.academy.service.impl;

import java.util.Collections;
import java.util.List;
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
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        ShoppingCart cart = shoppingCartDao.getByUser(user).orElseThrow(
                () -> new NoSuchElementException("Can't add ticket to cart, session: "
                        + movieSession
                        + " because shopping cart not register by this user: " + user));
        ticketDao.add(ticket);
        List<Ticket> cartTickets = cart.getTickets();
        cartTickets.add(ticket);
        cart.setTickets(cartTickets);
        shoppingCartDao.update(cart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(
                () -> new NoSuchElementException("Can't get shopping cart by user: " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setTickets(Collections.emptyList());
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        User userFromCart = shoppingCart.getUser();
        ShoppingCart cart = shoppingCartDao.getByUser(userFromCart).orElseThrow(
                () -> new NoSuchElementException("Can't clear shopping cart: " + shoppingCart));
        cart.setTickets(Collections.emptyList());
        shoppingCartDao.update(cart);
    }
}
