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
            throw new DataProcessingException("Can`t add shopping cart "
                    + shoppingCart + " to the DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        String query = "from ShoppingCart sc left join fetch sc.tickets t "
                + " left join fetch t.movieSession m"
                + " left join fetch m.cinemaHall "
                + " left join fetch m.movie where sc.user = :user";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> scQuery = session.createQuery(query, ShoppingCart.class);
            scQuery.setParameter("user", user);
            return scQuery.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can`t get shopping cart by user "
                    + user + " from the DB", e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ShoppingCart shoppingCartFromDb = getByUser(shoppingCart.getUser()).orElse(null);
            if (shoppingCartFromDb != null) {
                shoppingCartFromDb.setTickets(shoppingCart.getTickets());
                transaction = session.beginTransaction();
                session.update(shoppingCartFromDb);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Unable to update shopping cart", e);
        }
    }
}
