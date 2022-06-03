package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.ShoppingCardDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCard;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class ShoppingCardDaoImpl implements ShoppingCardDao {
    @Override
    public ShoppingCard add(ShoppingCard shoppingCard) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(shoppingCard);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert shopping card" + shoppingCard, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return shoppingCard;
    }

    @Override
    public Optional<ShoppingCard> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCard> query =
                    session.createQuery("FROM ShoppingCard sc "
                                    + "LEFT JOIN FETCH sc.tickets t "
                                    + "LEFT JOIN FETCH sc.user u "
                                    + "LEFT JOIN FETCH t.movieSession ms "
                                    + "LEFT JOIN FETCH ms.cinemaHall "
                                    + "LEFT JOIN FETCH ms.movie "
                                    + "WHERE u = :user", ShoppingCard.class);
            return query.setParameter("user", user).uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get a movie by user: " + user, e);
        }
    }

    @Override
    public void update(ShoppingCard shoppingCard) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(shoppingCard);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update shopping card" + shoppingCard, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
