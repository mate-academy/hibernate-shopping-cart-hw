package mate.academy.dao.impl;

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
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(shoppingCart);
            transaction.commit();
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert to DB user: "
                    + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> getByUserQuery = session.createQuery("SELECT DISTINCT s "
                    + "FROM ShoppingCart s "
                    + "LEFT JOIN FETCH s.tickets t "
                    + "LEFT JOIN FETCH t.movieSession m "
                    + "LEFT JOIN FETCH m.movie "
                    + "LEFT JOIN FETCH m.cinemaHall "
                    + "LEFT JOIN FETCH t.user "
                    + "LEFT JOIN FETCH s.user "
                    + "WHERE s.user = :user", ShoppingCart.class);
            getByUserQuery.setParameter("user", user);
            return getByUserQuery.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can`t get shopping  cart by user = " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
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
            throw new DataProcessingException("Can't update shoppingCart " + shoppingCart
                    + " in DB ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
