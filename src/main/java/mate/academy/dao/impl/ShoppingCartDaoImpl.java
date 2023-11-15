package mate.academy.dao.impl;

import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.Optional;

public class ShoppingCartDaoImpl implements ShoppingCartDao {
    private final SessionFactory factory = HibernateUtil.getSessionFactory();

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.persist(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new DataProcessingException(String.format(
                    "Can't add shopping cart: %s to Db", shoppingCart), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = factory.openSession()) {
            Query<ShoppingCart> query = session.createQuery(
                    "FROM shopping_cart sс WHERE sс.user = :user", ShoppingCart.class);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            throw new DataProcessingException("Can't get shopping cart by user: " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        try (Session session = factory.openSession()) {
            session.merge(shoppingCart);
        } catch (Exception e) {
            throw new DataProcessingException("Can't update shopping cart: " + shoppingCart, e);
        }
    }
}
