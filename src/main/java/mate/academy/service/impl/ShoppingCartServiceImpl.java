package mate.academy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private static final Logger logger = LogManager.getLogger(ShoppingCartServiceImpl.class);
    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        ShoppingCart shoppingCart = getByUser(user);
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ticket.setShoppingCart(shoppingCart);
        ticketDao.add(ticket);
        shoppingCart.addTicket(ticket);
        shoppingCartDao.update(shoppingCart);
        logger.info("Added new movieSession: " + movieSession + "of user: " + user);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(() ->
                new EntityNotFoundException("Can't get"
                + " shopping cart of user: " + user + "from DB", new Exception()));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart(user);
        shoppingCartDao.add(shoppingCart);
        logger.info("Registered new shopping cart for user :" + shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTicketList(Set.of());
        shoppingCartDao.update(shoppingCart);
        logger.info("Cleared shoppingCart :" + shoppingCart);
    }
}
