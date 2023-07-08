package mate.academy.service.impl;

import java.util.Collections;
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
import mate.academy.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private TicketDao ticketDao;
    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Override
    public void addSession(MovieSession movieSession, User user) throws RegistrationException {
        ShoppingCart shoppingCart = ifExists(user);
        Ticket ticket = ticketDao.add(new Ticket(movieSession, user));
        shoppingCart.getTickets().add(ticket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) throws RegistrationException {
        return ifExists(user);
    }

    @Override
    public void registerNewShoppingCart(User user) {
        shoppingCartDao.add(new ShoppingCart(user));
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(Collections.emptyList());
        shoppingCartDao.update(shoppingCart);
    }

    private ShoppingCart ifExists(User user) throws RegistrationException {
        Optional<ShoppingCart> shoppingCart = shoppingCartDao.getByUser(user);
        if (shoppingCart.isEmpty()) {
            throw new RegistrationException("Current user do not have registered carts. "
                    + "Please register the cart first.");
        }
        return shoppingCart.get();
    }
}
