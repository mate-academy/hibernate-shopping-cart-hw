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
    private TicketDao ticketDao;
    @Inject
    private ShoppingCartDao shoppingCartService;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket(movieSession, user);
        ticketDao.add(ticket);
        Optional<ShoppingCart> shoppingCart = shoppingCartService.getByUser(user);
        shoppingCartService.update(shoppingCart.get());
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartService.getByUser(user).orElseThrow();
    }

    @Override
    public void registerNewShoppingCart(User user) {
        shoppingCartService.add(new ShoppingCart(new ArrayList<Ticket>(), user));
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        List<Ticket> tickets = new ArrayList<>();
        shoppingCart.setTickets(tickets);
        shoppingCartService.update(shoppingCart);
    }
}
