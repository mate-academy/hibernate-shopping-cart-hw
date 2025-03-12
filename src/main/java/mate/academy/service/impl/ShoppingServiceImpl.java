package mate.academy.service.impl;

import mate.academy.dao.ShoppingDao;
import mate.academy.dao.TickedDao;
import mate.academy.dao.UserDao;
import mate.academy.dao.impl.ShoppingDaoImpl;
import mate.academy.dao.impl.TickedDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingService;

@Service
public class ShoppingServiceImpl implements ShoppingService {
    @Inject
    private final ShoppingDao shoppingDao = new ShoppingDaoImpl();
    @Inject
    private final TickedDao tickedDao = new TickedDaoImpl();
    @Inject
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setUser(userDao.findByEmail(user.getEmail()).get());
        ticket.setMovieSession(movieSession);
        tickedDao.add(ticket);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingDao.getByUser(user).get();
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(userDao.findByEmail(user.getEmail()).get());
        shoppingDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getTickets().clear();
        shoppingDao.update(shoppingCart);
    }
}
