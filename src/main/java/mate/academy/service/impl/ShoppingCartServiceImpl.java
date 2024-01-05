package mate.academy.service.impl;

import java.util.Collections;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.exception.EntityNotFoundException;
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
    public void addSession(MovieSession movieSession, User user) throws EntityNotFoundException {
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setOwner(user);
        ticketDao.add(ticket);

        ShoppingCart shoppingCart = getByUser(user);
        shoppingCart.addTicket(ticket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) throws EntityNotFoundException {
        return shoppingCartDao.getByUser(user)
                .orElseThrow(() ->
                        new EntityNotFoundException("Shopping cart not found for user: "
                                + user.getId()));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setOwner(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(Collections.emptyList());
        shoppingCartDao.update(shoppingCart);
    }
}
