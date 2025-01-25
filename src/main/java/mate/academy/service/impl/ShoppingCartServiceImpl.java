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
    private ShoppingCartDao cartDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        Ticket result = ticketDao.add(ticket);
        if (result == null) {
            throw new RuntimeException("Failed to add session ");
        }
        System.out.println("Session created");
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return cartDao.getByUser(user)
                .orElseThrow(() -> new RuntimeException("No cart found"));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cartDao.add(cart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        if (!shoppingCart.getTickets().isEmpty()) {
            shoppingCart.getTickets().clear();
            cartDao.update(shoppingCart);
        }
    }
}
