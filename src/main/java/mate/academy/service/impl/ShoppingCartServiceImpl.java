package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.MovieSessionDao;
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
    private MovieSessionDao movieSessionDao;
    @Inject
    private UserDao userDao;
    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        ticketDao.add(ticket);

        ShoppingCart shoppingCart = shoppingCartDao.getByUser(user)
                .orElseGet(() -> {
                        ShoppingCart newCart = new ShoppingCart();
                        newCart.setUser(user);
                        return shoppingCartDao.add(newCart);
                }
        );

        ticket.setShoppingCart(shoppingCart);
        shoppingCart.getTickets().add(ticket);
        ticketDao.add(ticket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        if (user == null) {
            throw new DataProcessingException("User can't be null");
        }
        if (user.getId() == null) {
            throw new DataProcessingException("User " + user.getId() + " doesn't exist");
        }
        return shoppingCartDao.getByUser(user);
    }

    @Override
    public void registerNewShoppingCart(User user) {
        userDao.add(user);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCartDao.update(shoppingCart);
    }
}
