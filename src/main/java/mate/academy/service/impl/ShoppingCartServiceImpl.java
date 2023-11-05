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
    @Inject
    private UserServiceImpl userService;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket newTicket = new Ticket();
        newTicket.setUser(user);
        newTicket.setMovieSession(movieSession);

        Optional<ShoppingCart> shoppingCart = shoppingCartDao.getByUser(user);
        if (shoppingCart.isPresent()) {
            ShoppingCart cart = shoppingCart.get();
            cart.getTickets().add(ticketDao.add(newTicket));
            shoppingCartDao.update(cart);
        } else {
            throw new RuntimeException("ShoppingCart is empty");
        }

    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElse(null);
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        Optional<User> userOptional = userService.findByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            shoppingCart.setUser(userOptional.get());
        } else {
            shoppingCart.setUser(user);
        }
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clearShoppingCart(ShoppingCart cart) {
        cart.setTickets(new ArrayList<>());
        shoppingCartDao.update(cart);

    }
}
