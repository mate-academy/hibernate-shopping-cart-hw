package mate.academy.service.impl;

import jakarta.persistence.EntityNotFoundException;
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

        ShoppingCart shoppingCart = getByUser(user);

        if (shoppingCart != null) {
            shoppingCart.getTickets().add(ticketDao.add(newTicket));
            shoppingCartDao.update(shoppingCart);
        } else {
            throw new RuntimeException("ShoppingCart is empty");
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Couldn't find user: "
                        + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        if (shoppingCartDao.getByUser(user).isPresent()) {
            throw new RuntimeException("Shopping cart already exist for user: "
            + user.getEmail());
        }
        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setUser(user);
        shoppingCartDao.add(newShoppingCart);
    }

    @Override
    public void clearShoppingCart(ShoppingCart cart) {
        cart.getTickets().clear();
        shoppingCartDao.update(cart);
    }
}
