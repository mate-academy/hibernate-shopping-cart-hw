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
            /* // I tried to convert hql variant to a criteria query,
            // but I didn't manage to do this. Please, tell me if you know how.
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ShoppingCart> query = criteriaBuilder.createQuery(ShoppingCart.class);
            Root<ShoppingCart> root = query.from(ShoppingCart.class);
            root.fetch("tickets", JoinType.LEFT);
            Root<Ticket> ticketRoot = query.from(Ticket.class);
            ticketRoot.fetch("movieSession", JoinType.LEFT);
            Root<MovieSession> movieSessionRoot = query.from(MovieSession.class);
            movieSessionRoot.fetch("cinemaHall", JoinType.LEFT);
            movieSessionRoot.fetch("movie", JoinType.LEFT);
            root.fetch("user", JoinType.LEFT);
            query.select(root).distinct(true).where(criteriaBuilder.equal(root.get("user"), user));
             */
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
