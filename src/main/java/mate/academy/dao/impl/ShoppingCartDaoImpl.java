package mate.academy.dao.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(shoppingCart);
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
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ShoppingCart> query = criteriaBuilder.createQuery(ShoppingCart.class);
        Root<ShoppingCart> scRoot = query.from(ShoppingCart.class);
        scRoot.fetch("tickets", JoinType.LEFT);
        query.distinct(true).select(scRoot).where(criteriaBuilder.equal(scRoot.get("user"), user));
        return session.createQuery(query).getResultStream().findFirst();
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaUpdate<ShoppingCart> update
                = criteriaBuilder.createCriteriaUpdate(ShoppingCart.class);
        Root<ShoppingCart> root = update.from(ShoppingCart.class);
        update.set("user", shoppingCart.getUser());
        update.set("tickets", shoppingCart.getTickets());
        update.where(criteriaBuilder.equal(root.get("id"), shoppingCart.getId()));
    }
}
