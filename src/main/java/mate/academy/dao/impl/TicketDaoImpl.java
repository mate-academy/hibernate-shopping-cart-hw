package mate.academy.dao.impl;

import mate.academy.dao.TicketDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Ticket;

@Dao
public class TicketDaoImpl extends AbstractDao implements TicketDao {
    @Override
    public Ticket add(Ticket ticket) {
        try {
            return performWithinTx(session -> {
                session.persist(ticket);
                return ticket;
            });
        } catch (Exception e) {
            throw new DataProcessingException("Can't add ticket to DB", e);
        }
    }
}
