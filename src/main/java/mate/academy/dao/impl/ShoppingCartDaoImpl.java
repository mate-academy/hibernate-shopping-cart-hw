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
            throw new DataProcessingException("Can't add " + ShoppingCart.class.getName()
                    + " of " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> getShoppingCartByUserQuery =
                    session.createQuery("from ShoppingCart sc "
                            + "join fetch sc.user u "
                            + "left join fetch sc.tickets t "
                            + "left join fetch t.movieSession ms "
                            + "left join fetch t.user "
                            + "left join fetch ms.cinemaHall "
                            + "left join fetch ms.movie "
                            + "where u.id = :id", ShoppingCart.class);
            getShoppingCartByUserQuery.setParameter("id", user.getId());
            return getShoppingCartByUserQuery.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get object of "
                    + ShoppingCart.class.getName()
                    + " by " + user, e);
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
                throw new DataProcessingException("Can't update "
                        + ShoppingCart.class.getName()
                        + " with " + shoppingCart, e);
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
