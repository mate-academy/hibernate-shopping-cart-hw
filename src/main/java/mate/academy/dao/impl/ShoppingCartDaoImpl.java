package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.HibernateException;
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
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add shopping cart to db " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> getShoppingCartQuery =
                    session.createQuery("FROM ShoppingCart sc "
                            + "LEFT JOIN FETCH sc.tickets t "
                            + "LEFT JOIN FETCH sc.user "
                            + "LEFT JOIN FETCH t.movieSession m "
                            + "LEFT JOIN FETCH t.user "
                            + "LEFT JOIN FETCH m.movie "
                            + "LEFT JOIN FETCH m.cinemaHall "
                            + "WHERE sc.user = :user", ShoppingCart.class);
            getShoppingCartQuery.setParameter("user", user);
            return getShoppingCartQuery.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get user for db " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.update(shoppingCart);
        } catch (Exception e) {
            throw new DataProcessingException("Can't update cart" + shoppingCart, e);
        }
    }
}
