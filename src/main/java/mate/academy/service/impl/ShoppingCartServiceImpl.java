package mate.academy.service.impl;

import java.util.ArrayList;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.ShoppingCartService;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Inject
    private TicketDao ticketDao;
    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private AuthenticationService authenticationService;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        if (user != null) {
            Ticket ticket = new Ticket();
            ticket.setMovieSession(movieSession);
            ticket.setUser(user);
            ticketDao.add(ticket);
            ShoppingCart shoppingCart = shoppingCartDao.getByUser(user).orElse(new ShoppingCart());
            shoppingCart.getTickets().add(ticket);
            shoppingCart.setUser(user);
            shoppingCartDao.update(shoppingCart);
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(
                () -> new RuntimeException("This shopping cart doesn't exist"));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        if (user != null) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            shoppingCart.setTickets(new ArrayList<>());
            shoppingCartDao.add(shoppingCart);
        } else {
            throw new RuntimeException("This user doesn't exist");
        }
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            shoppingCart.getTickets().clear();
            session.merge(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Couldn't clear shopping cart", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
