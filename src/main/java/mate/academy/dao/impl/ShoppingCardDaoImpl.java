package mate.academy.dao.impl;

import mate.academy.dao.ShoppingCardDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.ShoppingCard;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class ShoppingCardDaoImpl implements ShoppingCardDao {
    @Override
    public ShoppingCard add(ShoppingCard shoppingCart) {
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
            throw new DataProcessingException("Can't insert shopping cart" + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public ShoppingCard getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCard> getShoppingCardQuery = session.createQuery("from ShoppingCard sc "
                    + "left join fetch sc.user "
                    + "left join fetch sc.tickets t "
                    + "left join fetch t.movieSession ms "
                    + "left join fetch t.user "
                    + "left join fetch ms.cinemaHall "
                    + "left join fetch ms.movie "
                    + "where sc.user = :user", ShoppingCard.class);
            getShoppingCardQuery.setParameter("user", user);
            return getShoppingCardQuery.getSingleResult();
        } catch (Exception e) {
            throw new DataProcessingException("An error occurred while processing "
                    + "query to get shopping card by user = " + user, e);
        }
    }

    @Override
    public void update(ShoppingCard shoppingCard) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(shoppingCard);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("An error occurred while processing "
                    + "query to update shopping card = " + shoppingCard, e);
        }
    }
}
