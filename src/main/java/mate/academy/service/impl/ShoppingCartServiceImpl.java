package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        ShoppingCart shoppingCart = getByUser(user);
        List<Ticket> cartTickets = shoppingCart.getTickets();
        if (cartTickets == null) {
            cartTickets = new ArrayList<>();
        }
        cartTickets.add(ticket);
        shoppingCart.setTickets(cartTickets);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        Optional<ShoppingCart> userCart = shoppingCartDao.getByUser(user);
        if (userCart.isEmpty()) {
            throw new RuntimeException("There is no user "
                    + user + " in the database");
        }
        return userCart.get();
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        Optional<ShoppingCart> cartByUser =
                shoppingCartDao.getByUser(shoppingCart.getUser());
        if (cartByUser.isEmpty()) {
            throw new RuntimeException("There is no shopping cart "
                    + shoppingCart + " in the database");
        }
        shoppingCart.setTickets(Collections.emptyList());
        shoppingCartDao.update(shoppingCart);
    }
}
