package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;
import org.hibernate.Hibernate;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private TicketDao ticketDao;
    @Inject
    private ShoppingCartDao cartDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Optional<ShoppingCart> shoppingCartOptional = cartDao.getByUser(user);

        final ShoppingCart shoppingCart = shoppingCartOptional.orElseThrow(() ->
                new DataProcessingException("Can't get shopping cart by user: " + user, null));

        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ticketDao.add(ticket);

        Hibernate.initialize(shoppingCart.getTickets());
        shoppingCart.getTickets().add(ticket);
        cartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return cartDao.getByUser(user)
                .orElseThrow(() -> new DataProcessingException("Shopping cart not found "
                        + "with user: " + user, null));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        Optional<ShoppingCart> cartOptional = cartDao.getByUser(user);
        if (cartOptional.isEmpty()) {
            ShoppingCart newCart = new ShoppingCart();
            newCart.setUser(user);
            cartDao.add(newCart);
        }
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        if (shoppingCart != null && shoppingCart.getTickets() != null) {
            shoppingCart.getTickets().clear();
            cartDao.update(shoppingCart);
        }
    }
}
