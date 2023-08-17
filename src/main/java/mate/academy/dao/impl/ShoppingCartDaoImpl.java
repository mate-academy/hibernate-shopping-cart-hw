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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(shoppingCart);
            transaction.commit();
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert to DB shopping cart: "
                    + shoppingCart, e);
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        String query = "FROM ShoppingCart sc "
                + "LEFT JOIN FETCH sc.user u "
                + "LEFT JOIN FETCH sc.tickets t "
                + "LEFT JOIN FETCH t.movieSession ms "
                + "LEFT JOIN FETCH ms.movie "
                + "LEFT JOIN FETCH ms.cinemaHall "
                + "WHERE sc.user = :user ";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> getCartByUserQuery = session.createQuery(query, ShoppingCart.class);
            getCartByUserQuery.setParameter("user", user);
            return getCartByUserQuery.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get shopping cart by user: " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update shopping cart: "
                    + shoppingCart, e);
        }
    }
}
