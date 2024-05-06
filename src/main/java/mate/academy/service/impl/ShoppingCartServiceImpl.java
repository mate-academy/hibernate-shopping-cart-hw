package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.dao.UserDao;
import mate.academy.dao.impl.ShoppingCartDaoImpl;
import mate.academy.dao.impl.TicketDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
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
    private TicketDao ticketDao = new TicketDaoImpl();
    @Inject
    private ShoppingCartDao shoppingCartDao = new ShoppingCartDaoImpl();
    @Inject
    private UserDao userDao = new UserDaoImpl();

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        ticketDao.add(ticket);
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket);
        ShoppingCart shoppingCart = user.getShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setTickets(ticketList);
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
            shoppingCart.setTickets(Collections.emptyList());
            shoppingCartDao.add(shoppingCart);
        }
        user.setShoppingCart(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        if (shoppingCartDao.getByUser(shoppingCart.getUser()).isPresent()) {
            shoppingCart.setId(shoppingCart.getUser().getId());
            shoppingCart.setUser(shoppingCart.getUser());
            shoppingCart.setTickets(Collections.emptyList());
            shoppingCartDao.update(shoppingCart);
        }
    }
}
