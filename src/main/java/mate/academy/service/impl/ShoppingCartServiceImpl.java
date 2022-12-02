package mate.academy.service.impl;

import java.util.ArrayList;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.exception.EntityNotFoundException;
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
        Ticket ticket = new Ticket(movieSession, user);
        ShoppingCart cart = getByUser(user);
        cart.getTickets().add(ticketDao.add(ticket));
        shoppingCartDao.update(cart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(
                () -> new EntityNotFoundException("ShoppingCart by user " + user)
        );
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart cart = new ShoppingCart(new ArrayList<>(), user);
        shoppingCartDao.add(cart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.update(shoppingCart);
    }
}
