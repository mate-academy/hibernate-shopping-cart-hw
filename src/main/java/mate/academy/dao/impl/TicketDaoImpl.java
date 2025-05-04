package mate.academy.dao.impl;

import mate.academy.dao.TicketDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Ticket;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class TicketDaoImpl extends AbstractDao implements TicketDao {

    @Override
    public Ticket add(Ticket ticket) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.save(ticket);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    "Can't add ticket: " + ticket + " to Database", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return ticket;
    }
}
