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
    private static final String CANT_ADD_TICKET_EXCEPTION_MESSAGE =
            "Can't add ticket to DB: ";
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
            throw new DataProcessingException(CANT_ADD_TICKET_EXCEPTION_MESSAGE + ticket, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return ticket;
    }
}
