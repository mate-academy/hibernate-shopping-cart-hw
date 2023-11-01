package mate.academy.dao.impl;

import mate.academy.dao.TicketDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Ticket;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Dao
public class TicketDaoImpl implements TicketDao {
    @Override
    public Ticket add(Ticket ticket) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(ticket);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                throw new DataProcessingException("Can't insert to DB ticket: " + ticket, e);
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return ticket;
    }
}
