package mate.academy.dao.impl;

import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.lib.Inject;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Inject
    private UserService userService;

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        Session session = null;
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
            throw new DataProcessingException("Can't insert shopping cart to DB" + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Query<ShoppingCart> query =
                    session.createQuery("select sc from ShoppingCart sc "
                            + "left join fetch sc.ticketList t "
                            + "left join fetch t.movieSession ms "
                            + "left join fetch ms.movie "
                            + "left join fetch ms.cinemaHall "
                            + "left join fetch sc.user as u "
                            + "where u.id = :id", ShoppingCart.class);
            query.setParameter("id", user.getId());
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find the shopping cart by the given user"
                    + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update shopping cart to DB" + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
