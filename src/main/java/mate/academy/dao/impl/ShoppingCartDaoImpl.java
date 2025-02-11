package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can`t save shopping cart: " + shoppingCart);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session
                    .createQuery("FROM ShoppingCart WHERE user = :user", ShoppingCart.class)
                    .setParameter("user", user).uniqueResult());
        } catch (Exception e) {
            throw new RuntimeException("Can`t get shopping cart of this user: " + user);
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
            throw new RuntimeException("Can`t update shopping cart: " + shoppingCart);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
