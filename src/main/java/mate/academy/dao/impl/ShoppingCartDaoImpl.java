package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.save(shoppingCart);
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Query<ShoppingCart> query = session.createQuery("FROM ShoppingCart WHERE user = :user",
                ShoppingCart.class);
        query.setParameter("user", user);
        return query.uniqueResultOptional();
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.update(shoppingCart);
    }
}
