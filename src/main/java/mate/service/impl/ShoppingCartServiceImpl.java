package mate.service.impl;

import mate.dao.ShoppingCartDao;
import mate.dao.TicketDao;
import mate.lib.Inject;
import mate.lib.Service;
import mate.model.MovieSession;
import mate.model.ShoppingCart;
import mate.model.Ticket;
import mate.model.User;
import mate.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket(movieSession,user);
        ticketDao.add(ticket);

        ShoppingCart shoppingCart = shoppingCartDao.getByUser(user).orElseThrow();
        shoppingCart.getTickets().add(ticket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow();
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);
    }
}
