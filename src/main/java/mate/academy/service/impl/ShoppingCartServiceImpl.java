package mate.academy.service.impl;

import java.util.List;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.exception.DataProcessingException;
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
        if (shoppingCartDao.getByUser(user).isPresent()) {
            Ticket ticket = new Ticket();
            ticket.setUser(user);
            ticket.setMovieSession(movieSession);
            Ticket ticketFromD = ticketDao.add(ticket);

            ShoppingCart shoppingCart = getByUser(user);
            shoppingCart.getTickets().add(ticketFromD);
            shoppingCartDao.update(shoppingCart);
        } else {
            throw new DataProcessingException(
                    "User: " + user + " don't have shopping cart.");
        }

    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao
                .getByUser(user)
                .orElseThrow(
                        () -> new RuntimeException("Can't find user shopping cart: " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        if (shoppingCartDao.getByUser(shoppingCart.getUser()).isPresent()) {
            shoppingCart.setTickets(List.of());
            shoppingCartDao.update(shoppingCart);
        } else {
            throw new DataProcessingException("Can't clear shopping cart: " + shoppingCart);
        }
    }
}
