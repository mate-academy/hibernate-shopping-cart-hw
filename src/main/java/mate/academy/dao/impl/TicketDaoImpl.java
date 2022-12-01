package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.TicketDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Ticket;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;

@Dao
public class TicketDaoImpl extends AbstractDaoImpl<Ticket> implements TicketDao {
    @Override
    public Optional<Ticket> get(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Ticket.class, id));
        } catch (Exception e) {
            throw new DataProcessingException("Can't get ticket by id:" + id, e);
        }
    }
}
