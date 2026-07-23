package mate.academy.service.impl;

import mate.academy.dao.MovieSessionDao;
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
    @Inject
    private MovieSessionDao movieSessionDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        ShoppingCart shoppingCart = shoppingCartDao.getByUser(user)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart().setUser(user);
                    return shoppingCartDao.add(newCart);
                });

        User freshUser = shoppingCart.getUser();
        Ticket ticket = new Ticket()
                .setMovieSession(movieSession)
                .setUser(freshUser);
        ticketDao.add(ticket);

        shoppingCart.addTicket(ticket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user)
                .orElseThrow(() -> new DataProcessingException(
                        "ShoppingCart not found for user: " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart().setUser(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);
    }
}
