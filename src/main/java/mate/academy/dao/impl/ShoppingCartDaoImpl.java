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
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(shoppingCart);
            transaction.commit();
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can`t add shopping cart "
                    + shoppingCart + " to the DB", e);
        }
    }

    @Override
    public Optional<ShoppingCart> findByUser(User user) {
        String query = " FROM ShoppingCart sc "
                + " LEFT JOIN FETCH sc.tickets t "
                + "LEFT JOIN FETCH t.movieSession m "
                + "LEFT JOIN FETCH m.cinemaHall "
                + "LEFT JOIN FETCH m.movie WHERE sc.user = :user";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> scQuery = session.createQuery(query, ShoppingCart.class);
            scQuery.setParameter("user", user);
            return scQuery.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can`t get shopping cart by user "
                    + user + " from the DB", e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Unable to update shopping cart", e);
        }
    }
}
