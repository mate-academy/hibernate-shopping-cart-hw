package mate.academy.service.impl;

import java.util.ArrayList;
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
        Ticket newTicket = new Ticket(movieSession, user);
        Ticket newTicketWithIdFromDb = ticketDao.add(newTicket);
        if (shoppingCartDao.getByUser(user).isEmpty()) {
            throw new RuntimeException("User with id: " + user.getId() + "isn't present");
        }
        ShoppingCart shoppingCartByUser = shoppingCartDao.getByUser(user).get();
        shoppingCartByUser.getTickets().add(newTicketWithIdFromDb);
        shoppingCartDao.update(shoppingCartByUser);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        if (shoppingCartDao.getByUser(user).isEmpty()) {
            throw new RuntimeException("User with id: " + user.getId() + "isn't present");
        }
        return shoppingCartDao.getByUser(user).get();
    }

    @Override
    public void registerNewShoppingCart(User user) {
        shoppingCartDao.add(new ShoppingCart(user));
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(new ArrayList<Ticket>());
        shoppingCartDao.update(shoppingCart);
    }
}
