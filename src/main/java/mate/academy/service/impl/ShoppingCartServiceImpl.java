package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.Objects;
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
        Objects.requireNonNull(movieSession, "Movie session can't be null");
        Objects.requireNonNull(user, "User can't be null");
        ShoppingCart cart;
        cart = shoppingCartDao.getByUser(user).orElseGet(
                () -> {
                    registerNewShoppingCart(user);
                    return shoppingCartDao.getByUser(user).get();
                });
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ticketDao.add(ticket);
        cart.getTickets().add(ticket);
        shoppingCartDao.update(cart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        Objects.requireNonNull(user, "User can't be null");
        return shoppingCartDao.getByUser(user).orElseThrow(
                () -> new RuntimeException("Shopping cart not found for user " + user)
        );
    }

    @Override
    public void registerNewShoppingCart(User user) {
        Objects.requireNonNull(user, "User can't be null");
        if (shoppingCartDao.getByUser(user).isPresent()) {
            return;
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        Objects.requireNonNull(shoppingCart, "Shopping cart can't be null");
        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);
    }
}
