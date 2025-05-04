package mate.academy.dao;

import mate.academy.lib.Dao;
import mate.academy.model.Ticket;

@Dao
public interface TicketDao {
    Ticket add(Ticket ticket);
}
