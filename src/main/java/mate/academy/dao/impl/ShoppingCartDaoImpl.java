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

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.refresh(shoppingCart.getUser());
            session.persist(shoppingCart);
            transaction.commit();
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add shopping cart: " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try {
            return HibernateUtil.getSessionFactory()
                    .fromSession(s -> s.createQuery(getByUserQuery(), ShoppingCart.class)
                            .setParameter("user", user).uniqueResultOptional());
        } catch (Exception e) {
            throw new DataProcessingException("No ShoppingCart for User" + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        try {
            HibernateUtil.getSessionFactory().inTransaction(s -> s.merge(shoppingCart));
        } catch (Exception e) {
            throw new DataProcessingException("Can't update shoppingCart: " + shoppingCart, e);
        }
    }

    private String getByUserQuery() {
        return "FROM ShoppingCart sc "
                + "JOIN FETCH sc.user u "
                + "LEFT JOIN FETCH sc.tickets t "
                + "JOIN FETCH t.movieSession ms "
                + "JOIN FETCH ms.movie "
                + "JOIN FETCH ms.cinemaHall "
                + "WHERE sc.user =:user";
    }
}
