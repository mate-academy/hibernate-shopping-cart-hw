package mate.academy.service.impl;

import java.util.Collections;
import java.util.List;
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
        if (shoppingCartDao.getByUser(user).isPresent()) {
            ShoppingCart shoppingCart = shoppingCartDao.getByUser(user).get();
            Ticket newTicket = ticketDao.add(new Ticket(movieSession, user));
            List<Ticket> newListTicket = shoppingCart.getTickets();
            newListTicket.add(newTicket);
            shoppingCart.setTickets(newListTicket);
            shoppingCartDao.update(shoppingCart);
        } else {
            throw new RuntimeException("Can't add session");
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        if (shoppingCartDao.getByUser(user).isPresent()) {
            return shoppingCartDao.getByUser(user).get();
        }
        throw new RuntimeException("Shopping cart by user: " + user + "is null");
    }

    @Override
    public void registerNewShoppingCart(User user) {
        shoppingCartDao.add(new ShoppingCart(user));
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(Collections.emptyList());
        shoppingCartDao.update(shoppingCart);
    }
}
