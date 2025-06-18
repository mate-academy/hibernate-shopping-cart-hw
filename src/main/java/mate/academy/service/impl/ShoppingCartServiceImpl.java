package mate.academy.service.impl;

import java.util.List;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.UserDao;
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
    private UserDao userDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        ShoppingCart shoppingCart = shoppingCartDao.getByUser(user).get();
        List<Ticket> tickets = shoppingCart.getTickets();

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        tickets.add(ticket);

        shoppingCart.setTickets(tickets);
        shoppingCartDao.update(shoppingCart);

    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).get();
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        if (userDao.findByEmail(user.getEmail()).isPresent()) {
            shoppingCart.setUser(user);
            shoppingCartDao.add(shoppingCart);
        }

    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        if (shoppingCartDao.getByUser(shoppingCart.getUser()).isPresent()) {
            shoppingCart.getTickets().clear();
        }
    }
}
