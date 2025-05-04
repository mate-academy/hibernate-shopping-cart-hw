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
            throw new DataProcessingException("Can't insert shoppingCart" + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> getUserSessionByShoppingCartQuery =
                    session.createQuery("FROM ShoppingCart sc "
                            + "LEFT JOIN FETCH sc.tickets tl "
                            + "LEFT JOIN FETCH sc.user "
                            + "LEFT JOIN FETCH tl.movieSession ms "
                            + "LEFT JOIN FETCH ms.movie "
                            + "LEFT JOIN FETCH ms.cinemaHall "
                            + "WHERE sc.user = :user", ShoppingCart.class);
            getUserSessionByShoppingCartQuery.setParameter("user", user);
            return getUserSessionByShoppingCartQuery.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get a shoppingCart session by user: "
                    + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.merge(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update shoppingCart" + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
