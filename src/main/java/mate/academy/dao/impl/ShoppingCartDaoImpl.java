package mate.academy.dao.impl;

import java.util.List;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Hibernate;
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
            session.persist(shoppingCart);
            transaction.commit();
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can`t add shopingCart: " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> shoppingCartByUserQuery = session.createQuery(
                    "SELECT DISTINCT sc FROM ShoppingCart sc "
                            + "LEFT JOIN FETCH sc.user "
                            + "LEFT JOIN FETCH sc.tickets "
                            + "WHERE sc.user.id = :id",
                    ShoppingCart.class);
            shoppingCartByUserQuery.setParameter("id", user.getId());
            Optional<ShoppingCart> optionalShoppingCart =
                    shoppingCartByUserQuery.uniqueResultOptional();
            List<Ticket> ticketList = optionalShoppingCart.get().getTickets();
            for (Ticket ticket : ticketList) {
                Hibernate.initialize(ticket.getMovieSession().getMovie());
                Hibernate.initialize(ticket.getMovieSession().getCinemaHall());
            }
            return optionalShoppingCart;
        } catch (Exception e) {
            throw new DataProcessingException("Can`t find shopping cart by user: " + user, e);
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
            throw new DataProcessingException("Can't find shopping cart: " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
