package mate.academy.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Dao;
import mate.academy.model.Ticket;

@Dao
public class TicketDaoImpl implements TicketDao {
    private static final Map<Long, Ticket> storage = new HashMap<>();
    private static long idCounter = 1;

    @Override
    public Ticket add(Ticket ticket) {
        ticket.setId(idCounter++);
        storage.put(ticket.getId(), ticket);
        return ticket;
    }
}
