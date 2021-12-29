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
            session.save(shoppingCart);
            transaction.commit();
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert shopping cart " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> getShoppingCartByUserIdQuery =
                    session.createQuery("SELECT DISTINCT sp FROM ShoppingCart sp "
                            + "LEFT JOIN FETCH sp.tickets t "
                            + "LEFT JOIN FETCH t.user u "
                            + "LEFT JOIN FETCH t.movieSession ms "
                            + "LEFT JOIN FETCH ms.movie m "
                            + "LEFT JOIN FETCH ms.cinemaHall c "
                            + "WHERE sp.user = :user ", ShoppingCart.class);
            getShoppingCartByUserIdQuery.setParameter("user", user);
            return getShoppingCartByUserIdQuery.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get a shopping cart by user id: "
                    + user.getId(), e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update shopping cart " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
