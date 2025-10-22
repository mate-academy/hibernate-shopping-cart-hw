package mate.academy.service.impl;

import mate.academy.dao.TicketDao;
import mate.academy.dao.impl.ShoppingCartDaoImpl;
import mate.academy.dao.impl.TicketDaoImpl;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private ShoppingCartDaoImpl shoppingCartDaoImpl;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        TicketDao ticketDao = new TicketDaoImpl();
        ticketDao.add(ticket);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDaoImpl.getByUser(user).get();
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartDaoImpl.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getTickets().clear();
        shoppingCartDaoImpl.update(shoppingCart);
    }
}
