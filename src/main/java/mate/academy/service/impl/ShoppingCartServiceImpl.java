package mate.academy.service.impl;

import java.util.List;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.dao.impl.ShoppingCartDaoImpl;
import mate.academy.dao.impl.TicketDaoImpl;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

public class ShoppingCartServiceImpl implements ShoppingCartService {

    private ShoppingCartDao shoppingCartDao = new ShoppingCartDaoImpl();
    private TicketDao ticketDao = new TicketDaoImpl();

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        ticketDao.add(ticket);

        ShoppingCart shoppingCart = shoppingCartDao.getByUser(user)
                .orElseThrow(() -> new RuntimeException("No shopping cart of user: " + user));

        List<Ticket> ticketsOfUser = shoppingCart.getTickets();
        ticketsOfUser.add(ticket);

        shoppingCart.setTickets(ticketsOfUser);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user)
                .orElseThrow(() -> new RuntimeException("No shopping cart of user: " + user));
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
