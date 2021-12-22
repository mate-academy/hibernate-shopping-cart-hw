package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private TicketDao ticketDao;
    private AuthenticationService authenticationService;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket newTicket = new Ticket();
        newTicket.setMovieSession(movieSession);
        newTicket.setUser(user);
        ticketDao.add(newTicket);
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartDao.getByUser(user);
        if (optionalShoppingCart.isPresent()) {
            ShoppingCart shoppingCart = optionalShoppingCart.get();
            List<Ticket> tickets = shoppingCart.getTickets();
            tickets.add(newTicket);
            shoppingCart.setTickets(tickets);
            shoppingCartDao.update(shoppingCart);
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        Optional<ShoppingCart> shoppingCartOptional = shoppingCartDao.getByUser(user);
        if (shoppingCartOptional.isPresent()) {
            return shoppingCartOptional.get();
        }
        throw new RuntimeException("Can't find an user "
                + user + " in a db!");
    }

    @Override
    public void registerNewShoppingCart(User user) throws RegistrationException {
        User newUser = authenticationService.register(user.getEmail(), user.getPassword());
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(newUser);
        shoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {

    }
}
