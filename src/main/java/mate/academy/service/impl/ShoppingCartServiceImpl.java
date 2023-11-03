package mate.academy.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import mate.academy.dao.ShoppingDao;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private static final String EXCEPTION_GET = "Can't get shoppingCart by user: ";
    private static final String EXCEPTION_CLEAR = "Can't clear shoppingCart: ";
    @Inject
    private TicketDao ticketDao;
    @Inject
    private ShoppingDao shoppingDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ticketDao.add(ticket);
            ShoppingCart shoppingCart = getByUser(user);
            shoppingCart.getTickets().add(ticket);
            shoppingDao.update(shoppingCart);
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingDao.getByUser(user)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format(EXCEPTION_GET + user)));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        Session session = null;
        try  {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            shoppingCart.setTickets(null);
            shoppingDao.update(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(String.format(EXCEPTION_CLEAR + shoppingCart, e));
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
