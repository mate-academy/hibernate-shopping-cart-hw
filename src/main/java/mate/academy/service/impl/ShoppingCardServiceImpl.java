package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.ShoppingCardDao;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCard;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCardService;

@Service
public class ShoppingCardServiceImpl implements ShoppingCardService {
    @Inject
    private ShoppingCardDao shoppingCardDao;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ticketDao.add(ticket);
        ShoppingCard shoppingCard = shoppingCardDao.getByUser(user).get();
        List<Ticket> tickets = shoppingCard.getTickets();
        tickets.add(ticket);
        shoppingCardDao.update(shoppingCard);
    }

    @Override
    public ShoppingCard getByUser(User user) {
        return shoppingCardDao.getByUser(user).get();
    }

    @Override
    public void registerNewShoppingCard(User user) {
        ShoppingCard shoppingCard = new ShoppingCard();
        shoppingCard.setUser(user);
        shoppingCardDao.add(shoppingCard);
    }

    @Override
    public void clear(ShoppingCard shoppingCard) {
        shoppingCard.setTickets(new ArrayList<>());
        shoppingCardDao.update(shoppingCard);
    }
}
