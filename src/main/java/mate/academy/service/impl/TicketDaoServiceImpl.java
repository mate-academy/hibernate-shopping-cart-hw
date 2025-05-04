package mate.academy.service.impl;

import mate.academy.dao.TicketDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Ticket;
import mate.academy.service.TicketDaoService;

@Service
public class TicketDaoServiceImpl implements TicketDaoService {
    @Inject
    private TicketDao ticketDao;

    @Override
    public Ticket add(Ticket ticket) {
        return ticketDao.add(ticket);
    }
}
