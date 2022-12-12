package mate.academy.service.impl;

import mate.academy.dao.MovieSessionDao;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.UserDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;
import javax.persistence.EntityNotFoundException;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private MovieSessionDao movieSessionDao;
    @Inject
    private UserDao userDao;
    @Inject
    private ShoppingCartDao shoppingCartDao;


    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ShoppingCart userShoppingCart = getByUser(user);
        userShoppingCart.getTickets().add(ticket);
        shoppingCartDao.update(userShoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(() ->
                new EntityNotFoundException("No shopping cart in DB for user " + user));
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
