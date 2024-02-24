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
    private final SessionFactory factory = HibernateUtil.getSessionFactory();

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.save(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("ShoppingCart wasn't saved, rollback" + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            } else {
                System.out.println("Warning. Session was not opened");
            }
        }
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Two ways to do the same thing using criteria or hql
            Query<ShoppingCart> queryHql = session.createQuery(
                    "FROM ShoppingCart e LEFT JOIN FETCH e.tickets "
                            + "WHERE e.owner = :usr", ShoppingCart.class);
            queryHql.setParameter("usr", user);
            return queryHql.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get a cart by user: " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.merge(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("ShoppingCart wasn't updated, rollback" + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            } else {
                System.out.println("Warning. Session was not opened");
            }
        }
    }
}
