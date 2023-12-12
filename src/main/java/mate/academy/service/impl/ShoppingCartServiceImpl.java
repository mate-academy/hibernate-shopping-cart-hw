package mate.academy.service.impl;

import jakarta.persistence.EntityNotFoundException;
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
        Optional<ShoppingCart> cartFromDbOptional = shoppingCartDao.getByUser(user);
        if (cartFromDbOptional.isEmpty()) {
            throw new EntityNotFoundException("User " + user + " is not registered");
        }
        ShoppingCart cartFromDb = cartFromDbOptional.get();
        cartFromDb.getTickets().add(ticket);
        shoppingCartDao.update(cartFromDb);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        Optional<ShoppingCart> cartOptional = shoppingCartDao.getByUser(user);
        return cartOptional.orElseThrow(() ->
                new EntityNotFoundException("User " + user + " is not registered"));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(user.getId());
        shoppingCart.setUser(user);
        List<Ticket> tickets = new ArrayList<>();
        shoppingCart.setTickets(tickets);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        ShoppingCart cartFromDb = shoppingCartDao.getByUser(shoppingCart.getUser())
                .orElseThrow(() ->
                        new EntityNotFoundException("Cart " + shoppingCart + " no exist."));
        cartFromDb.setTickets(new ArrayList<>());
        shoppingCartDao.update(cartFromDb);
    }
}
