package mate.academy.service.impl;

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
import org.hibernate.query.Query;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        ticketDao.add(ticket);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> query = session.createQuery(
                            "FROM ShoppingCart sc LEFT JOIN FETCH sc.tickets "
                                    + "WHERE sc.user = :user", ShoppingCart.class);
            query.setParameter("user", user);
            return query.getSingleResultOrNull();
        } catch (Exception e) {
            throw new DataProcessingException("Couldn't get user: " + user, e);
        }
    }

    @Override
    public void registerNewShoppingCart(User user) {
        Session session = null;
        Transaction transaction = null;
        try {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    "Couldn't register new shopping cart for user: " + user, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.remove(shoppingCart);
        } catch (Exception e) {
            throw new DataProcessingException("Couldn't remove shopping cart: " + shoppingCart, e);
        }
    }
}
