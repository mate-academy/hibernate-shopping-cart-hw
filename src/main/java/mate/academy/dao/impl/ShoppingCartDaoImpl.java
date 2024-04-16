package mate.academy.dao.impl;

import java.sql.PreparedStatement;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(shoppingCart);
            transaction.commit();
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Failed to create ShoppingCart " + shoppingCart, e);
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(ShoppingCart.class, user.getId()));
        } catch (Exception e) {
            throw new DataProcessingException("Failed to get ShoppingCart by user " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<ShoppingCart> query = session.createNativeQuery(
                    "UPDATE shopping_carts  SET tickets = :tickets, user = :user WHERE id= :id", ShoppingCart.class);
            query.setParameter("tickets", shoppingCart.getTickets());
            query.setParameter("user", shoppingCart.getUser());
            query.setParameter("id", shoppingCart.getId());
            query.executeUpdate();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Failed to update the ShoppingCart " + shoppingCart, e);
        }
    }
}
