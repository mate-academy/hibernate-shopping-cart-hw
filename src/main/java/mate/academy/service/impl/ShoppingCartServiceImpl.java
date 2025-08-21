package mate.academy.service.impl;

import java.util.ArrayList;
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
        ShoppingCart shoppingCart = getByUser(user);
        boolean alreadyAdded = shoppingCart.getTickets().stream()
                .anyMatch(t -> t.getMovieSession() != null
                        && t.getMovieSession().getId() != null
                        && t.getMovieSession().getId().equals(movieSession.getId()));
        if (alreadyAdded) {
            return;
        }
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ticket.setShoppingCart(shoppingCart);
        shoppingCart.getTickets().add(ticket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao
                .getByUser(user).orElseThrow(() -> new RuntimeException("Shopping Cart not found"));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart ensureCart(User user) {
        return shoppingCartDao.getByUser(user).orElseGet(() -> {
            registerNewShoppingCart(user);
            return getByUser(user);
        });
    }
}
