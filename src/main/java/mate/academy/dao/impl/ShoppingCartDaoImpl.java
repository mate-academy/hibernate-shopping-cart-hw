package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.DaoOperation;
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
    private final DaoOperation<ShoppingCart> daoOperation =
            new DaoOperation<>("Shopping cart");

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        return daoOperation.add(shoppingCart);
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> shoppingCartByUser =
                    session.createQuery("from ShoppingCart sc "
                            + "LEFT join fetch sc.tickets "
                            + "where sc.id = :id ",
                            ShoppingCart.class);
            shoppingCartByUser.setParameter("id", user.getId());
            return shoppingCartByUser.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException(
                    "Was not able to get shopping cart by user! User info: " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
//            session.update(shoppingCart);
            session.merge(shoppingCart);
            transaction.commit();
>>>>>>> 87d558aaff19608fa83b5976da3ff64e7b3e3fb0
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
