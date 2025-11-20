package mate.academy.service.impl;

import java.util.ArrayList;
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
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ticketDao.add(ticket);

        ShoppingCart shoppingCart = getByUser(user);
        List<Ticket> tickets = new ArrayList<>();
        List<Ticket> beforeAddTicket = shoppingCart.getTickets();
        if (beforeAddTicket != null) {
            tickets.addAll(beforeAddTicket);
            try {
                beforeAddTicket.clear();
            } catch (RuntimeException ignored) {
                // Cannot clear? Forget it!
            }
        }
        tickets.add(ticket);
        shoppingCart.setTickets(List.copyOf(tickets));
        tickets.clear();
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        Optional<ShoppingCart> shoppingCart = shoppingCartDao.getByUser(user);
        if (shoppingCart.isEmpty()) {
            registerNewShoppingCart(user);
            return getByUser(user);
        }
        return shoppingCart.get();
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(List.of());
        shoppingCartDao.update(shoppingCart);
    }
}
