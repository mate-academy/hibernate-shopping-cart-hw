package mate.academy.dao;

import java.util.List;
import mate.academy.model.Ticket;
import mate.academy.model.User;

public interface TicketDao {
    Ticket add(Ticket ticket);

    List<Ticket> getAllTicketsByUser(User user);
}
