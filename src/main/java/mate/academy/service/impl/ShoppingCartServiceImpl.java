package mate.academy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        if (movieSession.getShowTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Can`t add ticket`s time of movie session "
                + movieSession.getShowTime());
        }
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ticketDao.add(ticket);
        ShoppingCart shoppingCart = shoppingCartDao.getByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find shopping cart by"
                        + "user " + user));
        List<Ticket> tickets = shoppingCart.getTickets();
        tickets.add(ticket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(() -> new EntityNotFoundException(
                "Can`t find shopping card by user " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        if (user == null) {
            throw new RuntimeException("User can`t be null");
        }
        if (shoppingCartDao.getByUser(user).isPresent()) {
            throw new RuntimeException("User " + user + " is already have shopping cart");
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        if (shoppingCart == null) {
            throw new RuntimeException("Shopping cart must not be null");
        }
        shoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.update(shoppingCart);
    }
}
