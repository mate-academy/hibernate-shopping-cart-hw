package mate.academy.dao.impl;

import mate.academy.dao.DaoOperation;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Dao;
import mate.academy.model.Ticket;

@Dao
public class TicketDaoImpl implements TicketDao {
    DaoOperation<Ticket> daoOperation = new DaoOperation<>("Ticket");

    @Override
    public Ticket add(Ticket ticket) {
        return daoOperation.add(ticket);
    }
}
