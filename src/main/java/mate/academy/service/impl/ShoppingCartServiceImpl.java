package mate.academy.service.impl;

import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Override
    public void addSession(MovieSession movieSession, User user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(movieSession);
            transaction.commit();
            return movieSession;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert movie session" + movieSession, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return null;
    }

    @Override
    public void registerNewShoppingCart(User user) {

    }

    @Override
    public void clear(ShoppingCart shoppingCart) {

    }
}
