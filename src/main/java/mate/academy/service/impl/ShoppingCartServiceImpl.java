package mate.academy.service.impl;

import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
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
    private ShoppingCartDao shoppingCartDao;

    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        ShoppingCart shoppingCart = shoppingCartDao.getByUser(user)
                .orElseThrow(() -> new DataProcessingException(
                        "Shopping cart for user " + user.getId() + " not found", null));

        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ticket.setShoppingCart(shoppingCart);

        ticketDao.add(ticket);

        shoppingCart.addTicket(ticket);

        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user)
                .orElseThrow(() -> new DataProcessingException(
                        "Shopping cart for user " + user.getId() + " not found", null));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart cart = new ShoppingCart(user);
        shoppingCartDao.add(cart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);
    }
}

