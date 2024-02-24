package mate.academy.dao.impl;

import mate.academy.dao.TicketDao;
import mate.academy.lib.Dao;
import mate.academy.model.Ticket;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Dao
public class TicketDaoImpl implements TicketDao {
    private final SessionFactory factory = HibernateUtil.getSessionFactory();

    @Override
    public Ticket add(Ticket ticket) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.persist(ticket);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("ShoppingCart wasn't saved, rollback" + ticket, e);
        } finally {
            if (session != null) {
                session.close();
            } else {
                System.out.println("Warning. Session was not opened");
            }
        }
        return ticket;
    }

    @Override
    public void remove(Ticket ticket) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.remove(ticket);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("ShoppingCart wasn't saved, rollback" + ticket, e);
        } finally {
            if (session != null) {
                session.close();
            } else {
                System.out.println("Warning. Session was not opened");
            }
        }
    }
}
