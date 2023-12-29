package mate.academy.service.impl;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
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
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            ShoppingCart shoppingCart = shoppingCartDao.getByUser(user).orElseThrow(
                    () -> new NoSuchElementException("ShoppingCart of User - "
                            + user + " is not exist"));

            Ticket ticket = new Ticket();
            ticket.setUser(user);
            ticket.setMovieSession(movieSession);
            Ticket savedTicket = ticketDao.add(ticket);

            shoppingCart.getTickets().add(savedTicket);
            shoppingCartDao.update(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add Ticket to a ShoppingCart", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        Optional<ShoppingCart> shoppingCartOptional = shoppingCartDao.getByUser(user);
        return shoppingCartOptional.orElseThrow(
                () -> new NoSuchElementException("ShoppingCart does not exist fro User: " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(Collections.emptyList());
        shoppingCartDao.update(shoppingCart);
    }
}
