package mate.academy.service.impl;

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
    private TicketDao ticketDao;
    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        if (movieSession != null && user != null) {
            Ticket ticket = new Ticket();
            ticket.setUser(user);
            ticket.setMovieSession(movieSession);
            ticketDao.add(ticket);
            ShoppingCart shoppingCart = shoppingCartDao.getByUser(user).orElseGet(() -> {
                ShoppingCart newShoppingCart = new ShoppingCart();
                newShoppingCart.setUser(user);
                shoppingCartDao.add(newShoppingCart);
                return newShoppingCart;
            });
            shoppingCart.getTicketList().add(ticket);
            shoppingCartDao.update(shoppingCart);
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        if (user != null) {
            return shoppingCartDao.getByUser(user).orElseThrow(
                    () -> new RuntimeException("Can't find user: " + user));
        }
        throw new RuntimeException("User can't be null!");
    }

    @Override
    public void registerNewShoppingCart(User user) {
        if (user != null && shoppingCartDao.getByUser(user).isEmpty()) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            shoppingCartDao.add(shoppingCart);
        } else {
            throw new RuntimeException("User is null or shopping cart already exists!");
        }
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        if (shoppingCart != null) {
            shoppingCart.getTicketList().clear();
            shoppingCartDao.update(shoppingCart);
        } else {
            throw new RuntimeException("Shopping cart can't be null!");
        }
    }
}
