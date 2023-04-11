package mate.academy.dao.impl;

import java.util.Optional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
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
            throw new DataProcessingException("Can't add shopping cart to DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            return session.createQuery("select distinct s from ShoppingCart s " +
                    "left join fetch s.tickets t " +
                    "left join fetch t.movieSession ms " +
                    "left join fetch ms.cinemaHall " +
                    "left join fetch ms.movie " +
                    "left join fetch s.user " +
                    "where s.user = :user", ShoppingCart.class).setParameter("user", user).uniqueResultOptional();
//            CriteriaBuilder cb = session.getCriteriaBuilder();
//            CriteriaQuery<ShoppingCart> getByUserQuery =
//                    cb.createQuery(ShoppingCart.class);
//            Root<ShoppingCart> root = getByUserQuery.from(ShoppingCart.class);
//            Predicate equal = cb.equal(root.get("user"), user);
//            Fetch<Object, Object> tickets = root.fetch("tickets", JoinType.LEFT);
//            tickets.fetch("movieSession");
//            root.fetch("user");
//            getByUserQuery.distinct(true).select(root).where(equal);
//            return session.createQuery(getByUserQuery).uniqueResultOptional();
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update shopping cart", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
