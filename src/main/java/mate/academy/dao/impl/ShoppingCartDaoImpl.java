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

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
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
            throw new DataProcessingException(
                    "Can't add a shopping cart to DB: " + shoppingCart, e
            );
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        String getShoppingCartByUserHql = "FROM ShoppingCart sc "
                + "LEFT JOIN FETCH sc.user u "
                + "LEFT JOIN FETCH sc.tickets t "
                + "LEFT JOIN FETCH t.movieSession ms "
                + "LEFT JOIN FETCH ms.movie m "
                + "LEFT JOIN FETCH ms.cinemaHall ch "
                + "WHERE sc.user = :user ";
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(getShoppingCartByUserHql, ShoppingCart.class)
                    .setParameter("user", user).uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException(
                    "Can't get a shopping cart by user: " + user, e
            );
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
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
            throw new DataProcessingException(
                    "Can't update a shopping cart in DB: " + shoppingCart, e
            );
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
