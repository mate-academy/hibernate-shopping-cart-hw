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
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.merge(shoppingCart);
            //session.persist(shoppingCart);
            transaction.commit();
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert ShoppingCart" + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        /*CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ShoppingCart> query = criteriaBuilder.createQuery(ShoppingCart.class);
        Root<ShoppingCart> scRoot = query.from(ShoppingCart.class);
        scRoot.fetch("tickets", JoinType.LEFT);
        query.distinct(true).select(scRoot).where(criteriaBuilder.equal(scRoot.get("user"), user));
        return session.createQuery(query).getResultStream().findFirst();*/
        Query<ShoppingCart> query = session.createQuery(("FROM ShoppingCart shc "
                + "left join fetch shc.user u left join fetch  shc.tickets t "
                + "WHERE u.id = :id").formatted(), ShoppingCart.class);
        query.setParameter("id", user.getId());
        return query.uniqueResultOptional();
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        /*Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaUpdate<ShoppingCart> update
                = criteriaBuilder.createCriteriaUpdate(ShoppingCart.class);
        Root<ShoppingCart> root = update.from(ShoppingCart.class);
        update.set("user", shoppingCart.getUser());
        update.set("tickets", shoppingCart.getTickets());
        update.where(criteriaBuilder.equal(root.get("id"), shoppingCart.getId()));*/
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.merge(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update shoppingCart " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
