package mate.academy.dao;

import java.util.List;
import mate.academy.model.Ticket;

public interface TicketDao {
    Ticket add(Ticket ticket);

    List<Ticket> getAll();

}
