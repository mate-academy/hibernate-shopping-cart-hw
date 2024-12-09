package mate.academy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
            throw new RuntimeException("Can't add ticket time of movieSession expired: "
                    + movieSession.getShowTime());
        }
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ticketDao.add(ticket);
        ShoppingCart shoppingCart = getByUser(user);
        shoppingCart.getTickets().add(ticket);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(() ->
                new EntityNotFoundException("Can't find shopping cart for user: " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        if (user == null) {
            throw new RuntimeException("User cannot be null");
        }
        if (shoppingCartDao.getByUser(user).isPresent()) {
            throw new RuntimeException("Shopping cart for user with id:" + user.getId()
                    + " already exists");
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        if (shoppingCart == null) {
            throw new RuntimeException("Shopping cart cannot be null");
        }
        shoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.update(shoppingCart);
    }
}
