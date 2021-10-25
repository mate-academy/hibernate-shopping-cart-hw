package mate.academy.service.impl;

import java.util.ArrayList;
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
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        ticketDao.add(ticket);
        Optional<ShoppingCart> userFromDbOptional = shoppingCartDao.getByUser(user);
        if (userFromDbOptional.isPresent()) {
            ShoppingCart shoppingCart = userFromDbOptional.get();
            shoppingCart.getTickets().add(ticket);
            shoppingCartDao.update(shoppingCart);
            return;
        }
        ShoppingCart shoppingCart = createNewShoppingCart(user);
        createNewShoppingCart(user).getTickets().add(ticket);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).get();
    }

    @Override
    public void registerNewShoppingCart(User user) {
        shoppingCartDao.add(createNewShoppingCart(user));
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.update(shoppingCart);
    }

    private ShoppingCart createNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        ArrayList<Ticket> tickets = new ArrayList<>();
        shoppingCart.setTickets(tickets);
        return shoppingCart;
    }
}
