package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(shoppingCart);
            transaction.commit();
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert shopping cart " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> query = session.createQuery("FROM ShoppingCart cart "
                    + "LEFT JOIN FETCH cart.tickets "
                    + "LEFT JOIN FETCH cart.tickets.movieSession "
                    + "LEFT JOIN FETCH cart.tickets.movieSession.movie "
                    + "LEFT JOIN FETCH cart.tickets.movieSession.cinemaHall "
                    + "WHERE cart.user = :user", ShoppingCart.class);
            query.setParameter("user", user);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find shopping cart by user: " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            if (session.get(ShoppingCart.class, shoppingCart.getId()) == null) {
                throw new NoSuchElementException("Shopping cart: " + shoppingCart
                        + " doesn't exist.");
            }
            transaction = session.beginTransaction();
            session.merge(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update shopping cart " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
