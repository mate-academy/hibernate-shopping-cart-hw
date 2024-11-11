package mate.academy.service.impl;

import java.util.List;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        /**
         * This method is responsible for adding a Ticket to the ShoppingCart
         * @param movieSession contains the information required for the ticket
         * @param user - the User who wants to buy the ticket for a specific movieSession
         */
        Optional<ShoppingCart> shoppingCartbyUserOptional = shoppingCartDao.getByUser(user);
        Ticket newTicket = new Ticket(movieSession,user);
        ticketDao.add(newTicket);
        shoppingCartbyUserOptional.orElseThrow().getTickets().add(newTicket);
        shoppingCartDao.update(shoppingCartbyUserOptional.orElseThrow());
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow();
    }

    @Override
    public void registerNewShoppingCart(User user) {
        shoppingCartDao.add(new ShoppingCart(user));
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        User user = shoppingCart.getUser();
        Optional<ShoppingCart> byUser = shoppingCartDao.getByUser(user);
        List<Ticket> tickets = byUser.orElseThrow().getTickets();
        tickets.clear();
        shoppingCartDao.update(shoppingCart);
    }
}
