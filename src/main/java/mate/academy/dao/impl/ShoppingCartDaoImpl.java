package mate.academy.dao.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
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
            User user = shoppingCart.getUser();
            if (user.getId() == null) {
                session.persist(user);
            } else {
                user = session.merge(user);
            }
            shoppingCart.setUser(user);
            session.persist(shoppingCart);
            transaction.commit();
            return shoppingCart;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save shopping cart: "
                    + shoppingCart, ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ShoppingCart> criteriaQuery = cb.createQuery(ShoppingCart.class);
            Root<ShoppingCart> root = criteriaQuery.from(ShoppingCart.class);
            Predicate userPredicate = cb.equal(root.get("user"), user);
            criteriaQuery.where(userPredicate);
            return session.createQuery(criteriaQuery).uniqueResultOptional();
        } catch (Exception ex) {
            throw new DataProcessingException("Can't find shopping cart for user:" + user, ex);
        }
    }

    @Override
    public ShoppingCart upDate(ShoppingCart shoppingCart) {
        Long id = shoppingCart.getId();
        List<Ticket> tickets = shoppingCart.getTickets();
        User userUpdated = shoppingCart.getUser();

        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            ShoppingCart currentCart = session.get(ShoppingCart.class, id);
            currentCart.setTickets(tickets);
            currentCart.setUser(userUpdated);
            transaction.commit();
            return currentCart;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update the shopping cart: "
                    + shoppingCart, ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
