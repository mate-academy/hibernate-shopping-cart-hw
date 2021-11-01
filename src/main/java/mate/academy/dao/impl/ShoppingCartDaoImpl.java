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
import org.hibernate.query.Query;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    private static SessionFactory factory = HibernateUtil.getSessionFactory();

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
            throw new DataProcessingException("Can't add shopping cart to DB with param "
                    + shoppingCart + ". ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        String query = "FROM ShoppingCart sc "
                + "left join fetch sc.tickets t "
                + "left join fetch t.movieSession ms "
                + "left join fetch t.user "
                + "left join fetch ms.movie "
                + "left join fetch ms.cinemaHall "
                + "where sc.user = :user";
        try (Session session = factory.openSession()) {
            Query<ShoppingCart> queryGetByUser = session.createQuery(query);
            queryGetByUser.setParameter("user", user);
            return queryGetByUser.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get shopping cart by user with email "
                    + user.getEmail() + ". ", e);
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
            throw new DataProcessingException("Can't update shopping cart with param "
                    + shoppingCart + ". ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
