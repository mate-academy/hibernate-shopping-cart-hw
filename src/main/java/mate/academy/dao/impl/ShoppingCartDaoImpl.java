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
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class ShoppingCartDaoImpl extends AbstractDao implements ShoppingCartDao {

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.save(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    "Can't add Shopping cart: " + shoppingCart + " to Database", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = factory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ShoppingCart> getByUser = cb.createQuery(ShoppingCart.class);
            Root<ShoppingCart> shoppingCarRoot = getByUser.from(ShoppingCart.class);
            Fetch<ShoppingCart, Ticket> ticketFetch =
                    shoppingCarRoot.fetch("tickets", JoinType.LEFT);
            shoppingCarRoot.fetch("user", JoinType.LEFT);
            Fetch<Ticket, MovieSession> movieSessionFetch =
                    ticketFetch.fetch("movieSession", JoinType.LEFT);
            movieSessionFetch.fetch("movie", JoinType.LEFT);
            movieSessionFetch.fetch("cinemaHall", JoinType.LEFT);
            getByUser.where(cb.equal(shoppingCarRoot.get("user"), user));
            return session.createQuery(getByUser).uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException(
                    "Can't get Shopping cart by user: " + user + "from Database ", e);
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
            throw new DataProcessingException(
                    "Can't update Shopping cart: " + shoppingCart + " in Database", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
