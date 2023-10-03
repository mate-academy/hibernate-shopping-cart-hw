package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.exception.DataProcessingException;
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
        ticket = ticketDao.add(ticket);

        Optional<ShoppingCart> shoppingCartByUser = shoppingCartDao.getByUser(user);
        if (shoppingCartByUser.isPresent()) {
            ShoppingCart shoppingCart = shoppingCartByUser.get();
            List<Ticket> tickets = new ArrayList<>(shoppingCart.getTickets());
            tickets.add(ticket);
            shoppingCart.setTickets(tickets);
            shoppingCartDao.update(shoppingCart);
        } else {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            shoppingCart.setTickets(List.of(ticket));
            shoppingCartDao.add(shoppingCart);
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(
                () -> new DataProcessingException("Can't find shopping cart by user " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setUser(user);
        newShoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.add(newShoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.update(shoppingCart);
    }
}
