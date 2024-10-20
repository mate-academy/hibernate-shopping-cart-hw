package mate.academy.service.impl;

import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.dao.UserDao;
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

    @Inject
    private UserDao userDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        if (userDao.findByEmail(user.getEmail()).isPresent()) {
            Ticket ticket = new Ticket(movieSession, user);
            ShoppingCart existing = getByUser(user);
            existing.getTickets().add(ticket);
            ticketDao.add(ticket);
            shoppingCartDao.update(existing);
            return;
        }

        throw new DataProcessingException("Cannot add a session to a shopping cart for a "
                + "non-existent user: " + user);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        if (userDao.findByEmail(user.getEmail()).isPresent()) {
            return shoppingCartDao.getByUser(user).orElseThrow(() ->
                    new DataProcessingException("No shopping cart found for user " + user));
        }

        throw new DataProcessingException("Cannot add a session to a shopping cart for a "
                + "non-existent user: " + user);
    }

    @Override
    public void registerNewShoppingCart(User user) {
            shoppingCartDao.add(new ShoppingCart(user));
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        ShoppingCart retrieved = getByUser(shoppingCart.getUser());
        retrieved.getTickets().clear();
        shoppingCartDao.update(retrieved);

    }
}
