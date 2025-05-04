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
            throw new DataProcessingException("Can't insert to DB shopping cart: "
                    + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Optional<ShoppingCart> optionalShoppingCart = session.createQuery("SELECT distinct sc "
                            + "FROM ShoppingCart sc "
                            + "LEFT JOIN FETCH sc.user "
                            + "LEFT JOIN FETCH sc.tickets "
                            + "WHERE sc.id = :id", ShoppingCart.class)
                    .setParameter("id", user.getId())
                    .uniqueResultOptional();
            if (optionalShoppingCart.isPresent()) {
                List<Ticket> tickets = optionalShoppingCart.get().getTickets();
                if (!tickets.isEmpty()) {
                    tickets.forEach(ticket -> {
                        Hibernate.initialize(ticket.getMovieSession().getMovie());
                        Hibernate.initialize(ticket.getMovieSession().getCinemaHall());
                    });
                }
            }
            return optionalShoppingCart;
        } catch (Exception e) {
            throw new DataProcessingException("Can't get shopping cart from DB by user: "
                    + user, e);
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
            throw new DataProcessingException("Can't update in DB shopping cart: "
                    + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
