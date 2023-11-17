package mate.academy.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import mate.academy.exception.DataProcessingException;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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

    public Optional<T> getById(Long id, Class<T> cls) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(cls, id));
        } catch (Exception e) {
            throw new DataProcessingException("Can't get a " + entityName + " by id: " + id, e);
        }
    }

    public List<T> getAll(String query, Class<T> cls) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<T> allEntitiesQuery = session.createQuery(query, cls);
            return allEntitiesQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get list of all " + entityName + "s.", e);
        }
    }

    public List<T> findAllByParams(Class<T> cls, Map<String, String[]> params) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(cls);
            Root<T> phoneRoot = query.from(cls);
            Iterator<Map.Entry<String, String[]>> iterator = params.entrySet().iterator();
            List<Predicate> predicates = new ArrayList<>();
            while (iterator.hasNext()) {
                Map.Entry<String, String[]> entry = iterator.next();
                CriteriaBuilder.In<String> predicate = cb.in(phoneRoot.get(entry.getKey()));
                for (String el : entry.getValue()) {
                    predicate.value(el);
                }
                predicates.add(predicate);
            }
            query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

            return session.createQuery(query).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can't find all in db with this input data: " + params, e);
        }
    }
}