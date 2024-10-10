package mate.academy.service.impl;

import mate.academy.dao.MovieSessionDao;
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
import mate.academy.security.AuthenticationService;
import mate.academy.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private AuthenticationService authenticationService;
    @Inject
    private MovieSessionDao movieSessionDao;
    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private TicketDao ticketDao;
    @Inject
    private UserDao userDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ticketDao.add(ticket);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(()
                -> new DataProcessingException("Could not get a user: " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        try {
            authenticationService.register(user.getEmail(), user.getPassword());
        } catch (RegistrationException e) {
            throw new RuntimeException("Could not register new shopping cart with user: "
                    + user, e);
        }
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCartDao.update(shoppingCart);
    }
}
