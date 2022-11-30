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
public class ShoppingCartDaoImpl extends GenericDaoImpl<ShoppingCart>
        implements ShoppingCartDao {
    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShoppingCart> query = session.createQuery(
                    "SELECT DISTINCT sc FROM ShoppingCart sc "
                    + "LEFT JOIN FETCH sc.tickets "
                    + "WHERE sc.user = :user", ShoppingCart.class);
            query.setParameter("user", user);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get shopping cart by user: " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.update(shoppingCart);
        } catch (Exception e) {
            throw new DataProcessingException("Can't update shopping cart: "
                    + shoppingCart, e);
        }
    }
}
