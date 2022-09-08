package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    private SessionFactory factory;

    public ShoppingCartDaoImpl() {
        factory = HibernateUtil.getSessionFactory();
    }

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.save(shoppingCart);
            transaction.commit();
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can`t save shopping cart to db " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        String query = "SELECT DISTINCT s "
                + "FROM ShoppingCart s "
                + "LEFT JOIN FETCH s.tickets t "
                + "LEFT JOIN FETCH t.user "
                + "LEFT JOIN FETCH t.movieSession ms "
                + "LEFT JOIN FETCH ms.movie "
                + "LEFT JOIN FETCH ms.cinemaHall "
                + "WHERE s.user = :user";
        try (Session session = factory.openSession()) {
            Query<ShoppingCart> getShoppingCartByUser = session
                    .createQuery(query, ShoppingCart.class);
            getShoppingCartByUser.setParameter("user", user);
            ShoppingCart shoppingCartByUser = getShoppingCartByUser.uniqueResult();
            Hibernate.initialize(shoppingCartByUser.getTickets());
            return Optional.ofNullable(shoppingCartByUser);
        } catch (Exception e) {
            throw new DataProcessingException("Can`t get shopping cart by user " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.update(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can`t update shopping cart " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
