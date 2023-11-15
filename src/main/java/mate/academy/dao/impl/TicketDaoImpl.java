package mate.academy.dao.impl;

import mate.academy.dao.TicketDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Ticket;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class TicketDaoImpl implements TicketDao {
    private final SessionFactory factory = HibernateUtil.getSessionFactory();

    @Override
    public Ticket add(Ticket ticket) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.persist(ticket);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new DataProcessingException(String.format(
                    "Can't add ticket: %s to DB", ticket), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return ticket;
    }
}
