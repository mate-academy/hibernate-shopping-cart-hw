package mate.academy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final TicketDao ticketDao;

    private final ShoppingCartDao shoppingCartDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ticketDao.add(ticket);

        ShoppingCart shopCartByUser = getByUser(user);
        shopCartByUser.setTickets(ticketDao.getAllTicketsByUser(user));
        shoppingCartDao.update(shopCartByUser);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user: " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        if (shoppingCartDao.getByUser(user).isEmpty()) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            shoppingCartDao.add(shoppingCart);
        }
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        List<Ticket> ticketList = shoppingCart.getTickets();
        shoppingCart.setTickets(Collections.emptyList());
        shoppingCartDao.update(shoppingCart);
        for (Ticket ticket : ticketList) {
            ticketDao.remove(ticket);
        }
    }
}
