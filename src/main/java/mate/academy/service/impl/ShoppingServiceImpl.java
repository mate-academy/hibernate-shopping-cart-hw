package mate.academy.service.impl;

import mate.academy.dao.ShoppingDao;
import mate.academy.dao.TicketDao;
import mate.academy.dao.UserDao;
import mate.academy.dao.impl.ShoppingDaoImpl;
import mate.academy.dao.impl.TicketDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingService;

@Service
public class ShoppingServiceImpl implements ShoppingService {

    private final ShoppingDao shoppingDao = new ShoppingDaoImpl();
    private final TicketDao tickedDao = new TicketDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setUser(userDao.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("can't find user in DB" + user)));
        ticket.setMovieSession(movieSession);
        tickedDao.add(ticket);
        ShoppingCart shoppingCart = getByUser(user);
        shoppingCart.getTickets().add(ticket);
        shoppingDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingDao.getByUser(user)
                .orElseThrow(() -> new RuntimeException("can't find user in DB" + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(userDao.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("can't find user in DB" + user)));
        shoppingDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getTickets().clear();
        shoppingDao.update(shoppingCart);
    }
}
