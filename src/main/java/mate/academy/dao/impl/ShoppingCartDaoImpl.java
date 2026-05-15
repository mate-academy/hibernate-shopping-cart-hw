package mate.academy.dao.impl;

import jakarta.persistence.NoResultException;
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
    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(shoppingCart);
            transaction.commit();
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add a shopping cart: " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<ShoppingCart> getShoppingCartQuery = session.createQuery(
                    "SELECT sc FROM ShoppingCart sc "
                            + "LEFT JOIN FETCH sc.tickets "
                            + "WHERE sc.user = :user", ShoppingCart.class);
            getShoppingCartQuery.setParameter("user", user);
            return Optional.ofNullable(getShoppingCartQuery.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
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
            throw new DataProcessingException("Can't update shopping cart: " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
