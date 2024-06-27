package mate.academy.service.impl;

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
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private TicketDao ticketDao;

    /**
     * This method is responsible for adding a Ticket to the ShoppingCart
     * @param movieSession contains the information required for the ticket
     * @param user - the User who wants to buy the ticket for a specific movieSession
     */
    @Override
    public void addSession(MovieSession movieSession, User user) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            ShoppingCart shoppingCart = session.createQuery("FROM "
                                                            + "ShoppingCart sc"
                                                            + " WHERE sc.user "
                                                            + "= :user",
                            ShoppingCart.class)
                    .setParameter("user", user)
                    .getSingleResult();
            if (shoppingCart == null) {
                throw new RuntimeException("ShoppingCart for user " + user + " not found");
            }
            Ticket ticket = new Ticket(movieSession, shoppingCart.getUser());
            ticketDao.add(ticket);
            session.merge(shoppingCart);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new RuntimeException("Can't add a ticket to the shopping cart", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public ShoppingCart getCartByUser(User user) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM ShoppingCart "
                                           + "scart WHERE scart.user = :user", ShoppingCart.class)
                    .setParameter("user", user)
                    .getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Can't find shoppingCart by user: " + user, e);
        }
    }

    @Override
    public void registerNewShoppingCart(User user) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            session.save(shoppingCart);
            user.setShoppingCart(shoppingCart);
            session.update(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't register a new shopping cart", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't clear the shopping cart", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
