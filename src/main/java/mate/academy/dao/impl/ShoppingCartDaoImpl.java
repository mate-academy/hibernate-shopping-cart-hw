package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can`t add shopping cart: " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<ShoppingCart> query = session.createQuery("SELECT DISTINCT s FROM ShoppingCart s "
                    + "LEFT JOIN FETCH s.tickets tic "
                    + "LEFT JOIN FETCH tic.movieSession mov "
                    + "LEFT JOIN FETCH mov.movie "
                    + "LEFT JOIN FETCH mov.cinemaHall "
                    + "WHERE s.user = :user", ShoppingCart.class);
            query.setParameter("user",user);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can`t find shopping cart by user: " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.update(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can`t update shopping cart: " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
