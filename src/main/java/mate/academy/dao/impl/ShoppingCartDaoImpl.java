package mate.academy.dao.impl;

import mate.academy.dao.*;
import mate.academy.exception.*;
import mate.academy.lib.*;
import mate.academy.model.*;
import mate.academy.util.*;
import org.hibernate.*;
import java.util.*;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(shoppingCart);
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
//            String hql = "FROM ShoppingCart sh join fetch sh.tickets where sh.user.id = :userId";
            String hql = "FROM ShoppingCart sh " +
                    "left join fetch sh.tickets t " +
                    "left join fetch t.movieSession ms " +
                    "left join fetch ms.cinemaHall " +
                    "left join fetch ms.movie " +
                    "where sh.user.id = :userId";
            return session.createQuery(hql, ShoppingCart.class)
                    .setParameter("userId", user.getId())
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new RuntimeException("TODO");
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
