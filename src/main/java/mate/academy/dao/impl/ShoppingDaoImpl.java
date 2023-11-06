package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.ShoppingDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class ShoppingDaoImpl implements ShoppingDao {
    private static final String GET_SHOPPING_CART = "FROM ShoppingCart s "
            + "LEFT JOIN FETCH s.tickets "
            + "LEFT JOIN FETCH s.user "
            + "WHERE s.user = :user ";
    private static final String EXCEPTION_ADD = "Can't add shoppingCart: ";
    private static final String EXCEPTION_UPDATE = "Can't update shoppingCart: ";

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            try {
                transaction = session.beginTransaction();
                session.save(shoppingCart);
                transaction.commit();
                return shoppingCart;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new DataProcessingException(EXCEPTION_ADD, e);
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> query = session.createQuery(GET_SHOPPING_CART,
                    ShoppingCart.class);
            query.setParameter("user", user);
            return query.uniqueResultOptional();
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            try {
                transaction = session.beginTransaction();
                session.update(shoppingCart);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new DataProcessingException(EXCEPTION_UPDATE, e);
            }
        }
    }
}
