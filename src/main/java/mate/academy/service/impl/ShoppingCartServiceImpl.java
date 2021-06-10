package mate.academy.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import mate.academy.dao.ShoppingCardDao;
import mate.academy.dao.TicketDao;
import mate.academy.exception.MovieSessionTimeException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCard;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCardService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCardService {
    @Inject
    private TicketDao ticketDao;
    @Inject
    private ShoppingCardDao shoppingCardDao;

    @Override
    public void addSession(MovieSession movieSession, User user) throws MovieSessionTimeException {
        if (movieSession.getShowTime().isAfter(LocalDateTime.now())) {
            ShoppingCard userShoppingCard = shoppingCardDao.getByUser(user);
            userShoppingCard.getTickets().add(ticketDao.add(new Ticket(movieSession, user)));
            shoppingCardDao.update(userShoppingCard);
        } else {
            throw new MovieSessionTimeException("The movie session time has passed");
        }
    }

    @Override
    public ShoppingCard getByUser(User user) {
        return shoppingCardDao.getByUser(user);
    }

    @Override
    public void registerNewShoppingCard(User user) {
        shoppingCardDao.add(new ShoppingCard(user));
    }

    @Override
    public void clear(ShoppingCard shoppingCard) {
        shoppingCard.setTickets(Collections.emptyList());
        shoppingCardDao.update(shoppingCard);
    }
}
