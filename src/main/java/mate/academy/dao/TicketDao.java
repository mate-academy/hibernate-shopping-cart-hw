package mate.academy.dao;

import mate.academy.model.Ticket;

public interface TicketDao {
    Ticket add(Ticket ticket);

    void remove(Ticket ticket);
}
