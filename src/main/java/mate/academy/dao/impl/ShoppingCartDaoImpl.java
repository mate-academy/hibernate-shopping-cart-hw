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
            throw new DataProcessingException("Can't insert shopping cart: "
                    + shoppingCart.toString(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> getShoppingCartByUser = session
                    .createQuery("from ShoppingCart s "
                            + "left join fetch s.tickets t "
                            + "left join fetch t.movieSession m "
                            + "left join fetch t.user "
                            + "left join fetch m.movie "
                            + "left join fetch m.cinemaHall "
                            + "where sc.user = :user", ShoppingCart.class);
            getShoppingCartByUser.setParameter("user", user);
            return Optional.ofNullable(getShoppingCartByUser.getSingleResult());
        } catch (Exception e) {
            throw new DataProcessingException("Can`t get shopping "
                    + "cart by user: " + user.toString(), e);
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
            throw new DataProcessingException("Can't insert "
                    + "shopping cart: " + shoppingCart.toString(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
