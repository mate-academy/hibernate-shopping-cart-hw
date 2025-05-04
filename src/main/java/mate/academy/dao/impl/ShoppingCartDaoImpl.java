package mate.academy.dao.impl;

import java.util.Objects;
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
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = getSession();
            transaction = session.beginTransaction();
            session.save(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    String.format("Can't add %s to DB", shoppingCart), e);
        } finally {
            if (Objects.nonNull(session)) {
                session.close();
            }
        }
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = getSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ShoppingCart> query = criteriaBuilder.createQuery(ShoppingCart.class);
            Root<ShoppingCart> shoppingCartRoot = query.from(ShoppingCart.class);
            shoppingCartRoot.fetch("user", JoinType.INNER);
            Fetch<ShoppingCart, Ticket> tickets = shoppingCartRoot.fetch("tickets", JoinType.LEFT);
            Fetch<Ticket, MovieSession> movieSession = tickets.fetch("movieSession", JoinType.LEFT);
            movieSession.fetch("movie", JoinType.LEFT);
            movieSession.fetch("cinemaHall", JoinType.LEFT);
            query.where(criteriaBuilder.equal(shoppingCartRoot.get("user"), user));
            return session.createQuery(query).uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException(
                    String.format("Can't get shopping cart by %s", user), e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = getSession();
            transaction = session.beginTransaction();
            session.update(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    String.format("Can't update %s in DB", shoppingCart), e);
        } finally {
            if (Objects.nonNull(session)) {
                session.close();
            }
        }
    }

    private Session getSession() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        return sessionFactory.openSession();
    }
}
