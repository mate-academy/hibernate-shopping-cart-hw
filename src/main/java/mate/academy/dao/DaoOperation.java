package mate.academy.dao;

import mate.academy.exception.DataProcessingException;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DaoOperation<T> {
    private final String entityName;

    public DaoOperation(String entityName) {
        this.entityName = entityName;
    }

    public T add(T entity) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert " + entityName + ": " + entity, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
