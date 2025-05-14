package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class ShoppingCartDaoImpl implements ShoppingCartDao {

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add ShoppingCart to the DB: "
                + shoppingCart, e);
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
            return session.createQuery(
                "select distinct s from ShoppingCart s "
                    + "left join fetch s.tickets "
                    + "where s.user = :user ", ShoppingCart.class)
                .setParameter("user", user)
                .uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Failed to getByUser the ShoppingCart "
                + "object with user: " + user, e);
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
            session.merge(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update ShoppingCart"
                + " to the DB: " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}

