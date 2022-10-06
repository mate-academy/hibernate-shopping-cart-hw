package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.dao.UserDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;
import mate.academy.util.HibernateUtil;
import org.hibernate.SessionFactory;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private TicketDao ticketDao;
    @Inject
    private UserDao userDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        ShoppingCart byUser = getByUser(user);
        List<Ticket> tickets = byUser.getTickets();
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        Ticket addedTicket = ticketDao.add(ticket);
        tickets.add(addedTicket);
        byUser.setTickets(tickets);
        shoppingCartDao.update(byUser);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(
                () -> new RuntimeException("Can`t get shopping cart by user: " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        List<Ticket> tickets = shoppingCart.getTickets();
        tickets = new ArrayList<>();
        shoppingCartDao.update(shoppingCart);
    }
}
