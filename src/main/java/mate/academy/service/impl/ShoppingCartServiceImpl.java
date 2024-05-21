package mate.academy.service.impl;

import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.DataProcessingException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.ShoppingCartService;
import java.util.ArrayList;
import java.util.List;


@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private TicketDao ticketDao;
    @Inject
    private UserDao userDao;
    @Inject
    private AuthenticationService authenticationService;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ticketDao.add(ticket);
        User userFromDB = userDao.findByEmail(user.getEmail()).get();
        ShoppingCart shoppingCart = shoppingCartDao.getByUser(userFromDB).get();
        List<Ticket> tickets = shoppingCart.getTickets();
        tickets.add(ticket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(()
                -> new DataProcessingException("Can`n get user " + user + " from DB"));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        User userFromDB;
        try {
            userFromDB = authenticationService.register(user.getEmail(), user.getPassword());
        } catch (RegistrationException e) {
            throw new DataProcessingException("This user has shopping cart " + user);
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(userFromDB);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.update(shoppingCart);
    }
}
