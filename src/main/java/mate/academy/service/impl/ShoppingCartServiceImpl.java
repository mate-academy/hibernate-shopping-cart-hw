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

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket saveTicket = new Ticket();
        saveTicket.setMovieSession(movieSession);
        saveTicket.setUser(user);
        ticketDao.add(saveTicket);
        ShoppingCart shoppingCart = shoppingCartDao.getByUser(user).orElseThrow(
                () -> new EntityNotFoundException("Can't add movie session to "
                        + "user's shopping cart with id: " + user.getId()));;
        shoppingCart.getTickets().add(saveTicket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).orElseThrow(
                () -> new EntityNotFoundException("Can't get shopping cart with user id "
                        + user.getId()));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);
    }
}
