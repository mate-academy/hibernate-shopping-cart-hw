package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    private final SessionFactory sessionFactory;

    public ShoppingCartDaoImpl() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(shoppingCart);
            transaction.commit();
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert shopping cart "
                    + shoppingCart + " to DB.", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try(Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session
                    .createQuery("FROM ShoppingCart AS sc "
                            + "LEFT JOIN FETCH sc.ticekts AS t "
                            + "LEFT JOIN FETCH t.movieSession AS ms "
                            + "LEFT JOIN FETCH ms.movie "
                            + "LEFT JOIN FETCH ms.cinemaHall "
                            + "LEFT JOIN FETCH sc.user.id =: userId", ShoppingCart.class)
                    .setParameter("userId", user.getId())
                    .getSingleResult());
        } catch (Exception e) {
            throw new DataProcessingException("Can't get shopping cart by user "
                    + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(shoppingCart);
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
