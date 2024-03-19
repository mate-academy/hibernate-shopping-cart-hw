package mate.academy.service.impl;

import jakarta.persistence.EntityNotFoundException;
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
        Optional<ShoppingCart> shoppingCartByUser = shoppingCartDao.getByUser(user);
        if (shoppingCartByUser.isPresent()) {
            Ticket newTicket = new Ticket();
            newTicket.setMovieSession(movieSession);
            newTicket.setUser(user);
            ticketDao.add(newTicket);
            shoppingCartByUser.get().getTickets().add(newTicket);
            shoppingCartDao.update(shoppingCartByUser.get());
            return;
        }
        throw new RuntimeException("Can't add Movie Session to Shopping Cart " + movieSession
                + " for User: " + user);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(
                () -> new EntityNotFoundException("Can't find Shopping Cart by User: " + user)
        );
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(List.of());
        shoppingCartDao.update(shoppingCart);
    }
}
