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
        /**
         * This method is responsible for adding a Ticket to the ShoppingCart
         * @param movieSession contains the information required for the ticket
         * @param user - the User who wants to buy the ticket for a specific movieSession
         */
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ticketDao.add(ticket);
        Optional<ShoppingCart> shoppingCartByUser = shoppingCartDao.getByUser(user);
        List<Ticket> ticketList = new ArrayList<>();
        shoppingCartByUser.get().setTickets(ticketList);
        shoppingCartDao.update(shoppingCartByUser.get());
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).get();
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {

    }
}
