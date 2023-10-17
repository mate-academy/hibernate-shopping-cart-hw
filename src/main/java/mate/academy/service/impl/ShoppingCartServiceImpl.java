package mate.academy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
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
        ticket.setOwner(user);
        ticket.setMovieSession(movieSession);
        ticketDao.add(ticket);
        ShoppingCart shoppingCart = getByUser(user);
        shoppingCart.getTickets().add(ticket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        Optional<ShoppingCart> userCart = shoppingCartDao.getByUser(user);
        return userCart.orElseThrow(() -> new EntityNotFoundException("There is no user "
                + user + " in the database"));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setOwner(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        ShoppingCart cartByUser = shoppingCartDao.getByUser(shoppingCart.getOwner())
                .orElseThrow(() -> new EntityNotFoundException("There is no shopping cart "
                        + shoppingCart + " in the database"));
        List<Ticket> tickets = cartByUser.getTickets();
        cartByUser.setTickets(Collections.emptyList());
        shoppingCartDao.update(cartByUser);
        ticketDao.removeList(tickets);
    }
}
