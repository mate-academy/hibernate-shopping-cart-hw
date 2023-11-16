package mate.academy.service.impl;

import java.util.ArrayList;
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
        Ticket currentSessionTicket = new Ticket();
        currentSessionTicket.setMovieSession(movieSession);
        currentSessionTicket.setUser(user);
        ticketDao.add(currentSessionTicket);

        ShoppingCart usersCart = getByUser(user);
        List<Ticket> updatedTickets = usersCart.getTickets();
        updatedTickets.add(currentSessionTicket);
        usersCart.setTickets(updatedTickets);
        shoppingCartDao.update(usersCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(
                () -> new RuntimeException(String.format(
                        "User: %s doesn't have a shopping car", user)));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart newCart = new ShoppingCart();
        newCart.setUser(user);
        shoppingCartDao.add(newCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        List<Ticket> emptyTicketList = new ArrayList<>();
        shoppingCart.setTickets(emptyTicketList);
        shoppingCartDao.update(shoppingCart);
    }
}
