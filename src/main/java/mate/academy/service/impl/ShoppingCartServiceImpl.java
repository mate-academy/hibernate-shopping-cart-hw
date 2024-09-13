package mate.academy.service.impl;

import jakarta.persistence.EntityNotFoundException;
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
        ticketDao.add(new Ticket(movieSession, user, getByUser(user)));
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(() ->
                new EntityNotFoundException("Can't get shoppingCart by user" + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        shoppingCartDao.add(new ShoppingCart(user));
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);
    }
}
