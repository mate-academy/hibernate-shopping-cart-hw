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
            throw new DataProcessingException(
                    "Can't insert shopping cart to DB. Shopping cart: " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> getShoppingCartByUserQuery = session.createQuery(
                    "SELECT sc FROM ShoppingCart sc "
                            + "LEFT JOIN FETCH sc.id "
                            + "LEFT JOIN FETCH sc.tickets t "
                            + "LEFT JOIN FETCH sc.user u "
                            + "LEFT JOIN FETCH u.id "
                            + "LEFT JOIN FETCH u.email "
                            + "LEFT JOIN FETCH t.id "
                            + "LEFT JOIN FETCH t.movieSession ms "
                            + "LEFT JOIN FETCH ms.id "
                            + "LEFT JOIN FETCH ms.showTime "
                            + "LEFT JOIN FETCH ms.cinemaHall ch "
                            + "LEFT JOIN FETCH ms.movie m "
                            + "LEFT JOIN FETCH ch.id "
                            + "LEFT JOIN FETCH ch.description "
                            + "LEFT JOIN FETCH ch.capacity "
                            + "LEFT JOIN FETCH m.id "
                            + "LEFT JOIN FETCH m.description "
                            + "LEFT JOIN FETCH m.title "
                            + "WHERE sc.user = :user", ShoppingCart.class);
            getShoppingCartByUserQuery.setParameter("user", user);
            return getShoppingCartByUserQuery.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find shopping cart by user: " + user, e);
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
            throw new DataProcessingException(
                    "Can't update shopping cart: " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
