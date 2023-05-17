package mate.academy.service.impl;

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
    private ShoppingCartDao cartDao;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket(movieSession, user);
        Optional<ShoppingCart> shoppingCartOptional = cartDao.getByUser(user);
        List<Ticket> tickets = shoppingCartOptional.get().getTickets();
        tickets.add(ticket);
        ticketDao.add(ticket);
        shoppingCartOptional.get().setTickets(tickets);
        cartDao.update(shoppingCartOptional.get());
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return cartDao.getByUser(user).get();
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cartDao.add(cart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getTickets().clear();
        cartDao.update(shoppingCart);
    }
}
