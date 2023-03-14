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
        } catch (DataProcessingException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add shoppingCart to DB" + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> shoppingCartQuery =
                    session.createQuery("FROM ShoppingCart sc "
                            + " LEFT JOIN FETCH sc.tickets t"
                            + " LEFT JOIN FETCH t.movieSession ms"
                            + " LEFT JOIN FETCH ms.movie m"
                            + " LEFT JOIN FETCH ms.cinemaHall"
                            + " LEFT JOIN FETCH sc.user"
                            + " WHERE sc.user.id = :id", ShoppingCart.class);
            shoppingCartQuery.setParameter("id", user.getId());
            return shoppingCartQuery.uniqueResultOptional();
        } catch (DataProcessingException e) {
            throw new DataProcessingException(
                    "Can't find shoppingCart into DB by this user" + user, e);
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
        } catch (DataProcessingException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update shoppingCart to DB" + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
