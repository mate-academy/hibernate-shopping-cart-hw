package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
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
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private AuthenticationService authenticationService;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        Ticket newTicket = ticketDao.add(ticket);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setTickets(List.of(newTicket));
        shoppingCart.setUser(newTicket.getUser());
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(()
                -> new NoSuchElementException("Not found ShoppingCart by user" + user));
    }

    @Override
    public void registerNewShoppingCart(User user) throws RegistrationException {
        authenticationService.register(user.getEmail(), user.getPassword());
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.update(shoppingCart);
    }
}
