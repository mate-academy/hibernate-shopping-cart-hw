package mate.academy.service.impl;

import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private TicketDao ticketDao;
    @Inject
    private AuthenticationService authenticationService;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        ticketDao.add(ticket);
        ShoppingCart byUser = getByUser(user);
        shoppingCartDao.update(byUser);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(
                () -> new DataProcessingException("No shopping cart found")
        );
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        User user = shoppingCart.getUser();
        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setUser(user);
        newShoppingCart.setId(shoppingCart.getId())
        shoppingCartDao.update(newShoppingCart);
    }
}
