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

import java.util.Collections;
import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket(movieSession, user);
        ticketDao.add(ticket);
        ShoppingCart userCart = getByUser(user);
        userCart.getTickets().add(ticket);
        shoppingCartDao.update(userCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartDao.getByUser(user);
        if (optionalShoppingCart.isEmpty()) {
            registerNewShoppingCart(user);
        }
        return optionalShoppingCart.get();
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart newShoppingCart = new ShoppingCart(Collections.emptyList(), user);
        shoppingCartDao.add(newShoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(Collections.emptyList());
        shoppingCartDao.update(shoppingCart);
    }
}
