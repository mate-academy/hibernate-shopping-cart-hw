package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Inject;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

@Inject
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartDao.getByUser(user);
        ShoppingCart shoppingCart;
        if (optionalShoppingCart.isPresent()) {
            shoppingCart = optionalShoppingCart.get();
        } else {
            registerNewShoppingCart(user);
            shoppingCart = shoppingCartDao.getByUser(user).get();
        }
        if (shoppingCart.getTickets() == null) {
            shoppingCart.setTickets(new ArrayList<>());
        }

        Ticket newTicket = new Ticket();
        newTicket.setUser(user);
        newTicket.setMovieSession(movieSession);
        ticketDao.add(newTicket);
        shoppingCart.getTickets().add(newTicket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(()
                -> new RuntimeException("No shopping cart found"));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setUser(user);
        newShoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.add(newShoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);
    }
}
