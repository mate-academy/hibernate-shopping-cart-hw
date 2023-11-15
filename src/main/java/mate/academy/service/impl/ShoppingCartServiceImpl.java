package mate.academy.service.impl;

import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartDao shoppingCartDao;
    private final TicketDao ticketDao;

    public ShoppingCartServiceImpl(ShoppingCartDao shoppingCartDao,
                                   TicketDao ticketDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.ticketDao = ticketDao;
    }

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket currentSessionTicket = new Ticket();
        currentSessionTicket.setMovieSession(movieSession);
        currentSessionTicket.setUser(user);
        Ticket addedTicket = ticketDao.add(currentSessionTicket);

        ShoppingCart usersCart = new ShoppingCart();
        usersCart.getTickets().add(addedTicket);
        usersCart.setUser(user);
        shoppingCartDao.add(usersCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(
                () -> new RuntimeException(String.format("User: %s is not exists", user)));
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
