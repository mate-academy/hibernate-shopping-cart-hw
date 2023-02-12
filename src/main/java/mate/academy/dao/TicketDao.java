package mate.academy.dao;

import mate.academy.model.MovieSession;
import mate.academy.model.Ticket;
import mate.academy.model.User;

public interface TicketDao {
    Ticket add(Ticket ticket);

    Ticket get(MovieSession movieSession, User user);
}
