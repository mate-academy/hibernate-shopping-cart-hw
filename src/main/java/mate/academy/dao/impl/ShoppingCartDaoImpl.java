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
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart add(ShoppingCart cart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(cart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save shopping cart to db" + cart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return cart;
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ShoppingCart> query = cb.createQuery(ShoppingCart.class);
            Root<ShoppingCart> root = query.from(ShoppingCart.class);
            Fetch<ShoppingCart, User> userFetch = root.fetch("user");
            Fetch<ShoppingCart, Ticket> ticketsFetch = root.fetch("tickets", JoinType.LEFT);
            Fetch<Ticket, MovieSession> movieSessionFetch = ticketsFetch.fetch("movieSession", JoinType.LEFT);
            Fetch<MovieSession, Movie> movieFetch = movieSessionFetch.fetch("movie", JoinType.LEFT);
            Fetch<MovieSession, CinemaHall> cinemaHallFetch = movieSessionFetch.fetch("cinemaHall", JoinType.LEFT);
            query.where(cb.equal(root.get("user"), user));
            return session.createQuery(query).uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get user from db.User: " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart cart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(cart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update shopping cart in db" + cart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
