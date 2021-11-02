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
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ShoppingCart getShoppingCartByUser = getByUser(user);
        if (getShoppingCartByUser == null) {
            throw new RuntimeException("User: " + user + " has not shopping cart. "
                    + "Please, register shopping cart.");
        }
        ticketDao.add(ticket);
        getShoppingCartByUser.setTickets(List.of(ticket));
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(() -> new RuntimeException("Can't"
                + " get shopping cart of user with Id " + user.getId()));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        if (shoppingCartDao.getByUser(user).isPresent()) {
            throw new RuntimeException("User with email: " + user.getEmail()
                    + " has not registered to DB."
                    + " The user already has the shopping cart");
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.update(shoppingCart);
    }
}
