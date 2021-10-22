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
            session.close();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add shopping cart: " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        Long id = user.getId();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<ShoppingCart> query
                    = session.createQuery("from ShoppingCart sc "
                    + "JOIN FETCH sc.user "
                    // + "JOIN FETCH sc.tickets " //why this line brings an empty optional?
                    + "where sc.user = :user", ShoppingCart.class);
            query.setParameter("user", user);
            Optional<ShoppingCart> shoppingCart = query.uniqueResultOptional();
            //Optional<ShoppingCart> shoppingCartEager
            // = Optional.ofNullable(session.get(ShoppingCart.class, user.getId()));
            return shoppingCart;
        } catch (Exception e) {
            throw new DataProcessingException("Can't get a shopping cart by user id: "
                + user.getId(), e);
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
            session.close();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update shopping cart: "
                    + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
