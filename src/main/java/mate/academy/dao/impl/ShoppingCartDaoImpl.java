package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(shoppingCart);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can`t add shoppingCart "
                    + shoppingCart + " to DB", ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        String query = "from ShoppingCart sc left join fetch sc.tickets t "
                + " left join fetch t.movieSession m"
                + " left join fetch m.cinemaHall "
                + " left join fetch m.movie where sc.user = :user";
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Query<ShoppingCart> shoppingCartQuery =
                    session.createQuery(query, ShoppingCart.class);
            shoppingCartQuery.setParameter("user", user);
            return Optional.ofNullable(shoppingCartQuery.getSingleResult());
        } catch (RuntimeException ex) {
            throw new DataProcessingException("Can not find ShoppingCart"
                    + "by user " + user, ex);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(shoppingCart);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can not update"
                    + " shoppingCart " + shoppingCart, ex);
        }
    }
}
