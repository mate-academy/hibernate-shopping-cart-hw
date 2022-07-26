package mate.academy.service.impl;

import java.util.Collections;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.exception.RegistrationException;
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
    private UserDao userDao;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        final ShoppingCart shoppingCart = shoppingCartDao.getByUser(user)
                .orElseThrow(() -> new DataProcessingException("No such user " + user + " in DB"));
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        ticket = ticketDao.add(ticket);
        shoppingCart.getTickets().add(ticket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        return shoppingCartDao.getByUser(user);
    }

    @Override
    public void registerNewShoppingCart(User user) throws RegistrationException {
        Optional<User> userFromDB = userDao.findByEmail(user.getEmail());
        if (userFromDB.isPresent()) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            shoppingCartDao.add(shoppingCart);
        } else {
            throw new RegistrationException("Cannot register shopping cart because user="
                    + user + " isn't exist in DB");
        }
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(Collections.emptyList());
        shoppingCartDao.update(shoppingCart);
    }
}
