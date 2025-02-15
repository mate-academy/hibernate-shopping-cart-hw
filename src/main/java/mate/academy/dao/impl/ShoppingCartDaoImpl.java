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

            if (shoppingCart.getUser() != null) {
                if (!session.contains(shoppingCart.getUser())) {
                    shoppingCart.setUser(
                            session.get(User.class, shoppingCart.getUser().getId()));
                }
                throw new DataProcessingException("User can't be null", null);
            }

            session.save(shoppingCart);
            transaction.commit();
            return shoppingCart;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    "Can't insert to DB shoppingCart: " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> getShoppingCartByUserOptional = session.createQuery(
                    "SELECT sc FROM ShoppingCart sc "
                            + "LEFT JOIN FETCH sc.tickets "
                            + "WHERE sc.user = :user",
                    ShoppingCart.class);

            getShoppingCartByUserOptional.setParameter("user", user);

            return getShoppingCartByUserOptional.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Cannot find shoppingCart by user: " + user, e);
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
                    "Cannot update DB with shoppingCart: " + shoppingCart, e
            );
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
