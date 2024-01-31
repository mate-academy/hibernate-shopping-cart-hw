package mate.academy.service.impl;

import java.util.Collections;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.exception.ShoppingCartException;
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
        Optional<ShoppingCart> shoppingCart = shoppingCartDao.getByUser(user);
        if (shoppingCart.isPresent()) {
            Ticket ticket = new Ticket();
            ticket.setUser(user);
            ticket.setMovieSession(movieSession);
            ticketDao.add(ticket);
            shoppingCart.get().getTickets().add(ticket);
            shoppingCartDao.update(shoppingCart.get());
        }
    }

    @Override
    public ShoppingCart getByUser(User user) throws ShoppingCartException {
        return shoppingCartDao.getByUser(user).orElseThrow(
                () -> new ShoppingCartException("Cannot find shopping cart for user: " + user));
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
        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);
    }
}
