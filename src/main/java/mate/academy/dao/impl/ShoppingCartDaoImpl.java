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
    private static final String GET_SHOPPING_CART_BY_USER_QUERY = """
                                                FROM ShoppingCart sc 
                                                LEFT JOIN FETCH sc.tickets t 
                                                LEFT JOIN FETCH  sc.user 
                                                LEFT JOIN t.movieSession ms 
                                                LEFT JOIN t.user u 
                                                LEFT JOIN ms.movie 
                                                LEFT JOIN ms.cinemaHall 
                                                WHERE sc.user = :user""";
    private static final String CANT_ADD_SHOPPING_CART_EXCEPTION_MESSAGE =
            "Can't add ShoppingCart to DB: ";

    private static final String USER_PARAMETER = "user";
    private static final String CANT_UPDATE_SHOPPING_CART_EXCEPTION_MESSAGE =
            "Can't update shopping cart: ";
    private final SessionFactory factory = HibernateUtil.getSessionFactory();

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.persist(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    CANT_ADD_SHOPPING_CART_EXCEPTION_MESSAGE + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = factory.openSession()) {
            Query<ShoppingCart> userShoppingCart = session.createQuery(
                    GET_SHOPPING_CART_BY_USER_QUERY, ShoppingCart.class);
            userShoppingCart.setParameter(USER_PARAMETER, user);
            return userShoppingCart.uniqueResultOptional();
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.merge(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    CANT_UPDATE_SHOPPING_CART_EXCEPTION_MESSAGE + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}