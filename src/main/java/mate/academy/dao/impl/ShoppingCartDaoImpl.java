package mate.academy.dao.impl;

import java.util.Optional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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
            throw new DataProcessingException("Cannot save to DB shoppingCart: " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        String hql = "SELECT DISTINCT sc "
                + "FROM ShoppingCart sc "
                + "LEFT JOIN FETCH sc.tickets t "
                + "LEFT JOIN FETCH t.movieSession ms "
                + "LEFT JOIN FETCH ms.cinemaHall ch "
                + "LEFT JOIN FETCH ms.movie "
                + "LEFT JOIN FETCH sc.user "
                + "WHERE sc.user = :user";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ShoppingCart> query = criteriaBuilder.createQuery(ShoppingCart.class);
            Root<ShoppingCart> root = query.from(ShoppingCart.class);
            Fetch<ShoppingCart, Ticket> ticketFetch = root.fetch("tickets", JoinType.LEFT);
            Fetch<Ticket, MovieSession> movieSessionFetch = ticketFetch
                    .fetch("movieSession", JoinType.LEFT);
            movieSessionFetch.fetch("cinemaHall", JoinType.LEFT);
            movieSessionFetch.fetch("movie", JoinType.LEFT);
            root.fetch("user", JoinType.LEFT);
            query.select(root).distinct(true).where(criteriaBuilder.equal(root.get("user"), user));
            Query<ShoppingCart> hqlQuery = session.createQuery(hql, ShoppingCart.class);
            hqlQuery.setParameter("user", user);
            return Optional.ofNullable(hqlQuery.getSingleResult());
        } catch (Exception e) {
            throw new DataProcessingException("Cannot get shopping cart by user: " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.merge(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Cannot merge shoppingCart: " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
