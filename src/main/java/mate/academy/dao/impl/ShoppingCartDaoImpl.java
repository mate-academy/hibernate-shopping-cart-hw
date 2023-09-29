package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

@Dao
public class ShoppingCartDaoImpl extends AbstractDao implements ShoppingCartDao {
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        try {
            return performWithinTx(session -> {
                session.save(shoppingCart);
                return shoppingCart;
            });
        } catch (Exception e) {
            throw new DataProcessingException("Can't add shopping cart to DB", e);
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return getByUserQuery(session).setParameter("user", user).uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException(
                    "Can't get shopping cart for user with id=%d".formatted(user.getId()), e
            );
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        try {
            performWithinTx(session -> session.merge(shoppingCart));
        } catch (Exception e) {
            throw new DataProcessingException("Can't update shopping cart to DB", e);
        }
    }

    private Query<ShoppingCart> getByUserQuery(Session session) {
        return session.createQuery("""
           from ShoppingCart c
           join fetch c.user
           left join fetch c.tickets ct
           left join fetch ct.movieSession ms
           left join fetch ms.movie
           left join fetch ms.cinemaHall
           where c.user = :user""", ShoppingCart.class);
    }
}
