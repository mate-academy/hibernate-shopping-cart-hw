package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.lib.Inject;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addTicket(Ticket ticket) {
        ticketDao.add(ticket);
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from ShoppingCart "
                            + "where user = :user")
                    .setParameter("user", user)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get shopping cart by user " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart cart) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ShoppingCart oldCart = getByUser(cart.getUser()).get();
            oldCart.setUser(cart.getUser());
            oldCart.setTickets(cart.getTickets());
            session.merge(oldCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update shopping cart " + cart, e);
        }
    }

    @Override
    public void register(ShoppingCart cart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(cart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't register new shopping cart " + cart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ShoppingCart oldCart = getByUser(shoppingCart.getUser()).get();
            oldCart.setTickets(null);
            oldCart.setUser(null);
            session.merge(oldCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't clear shopping cart " + shoppingCart, e);
        }
    }
}
