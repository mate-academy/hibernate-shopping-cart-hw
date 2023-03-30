package mate.academy.service.impl;

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
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    /**
     * This method is responsible for adding a Ticket to the ShoppingCart
     * @param movieSession contains the information required for the ticket
     * @param user - the User who wants to buy the ticket for a specific movieSession
     */
    @Inject
    TicketDao ticketDao;
    @Inject
    ShoppingCartDao shoppingCartDao;
    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        ticketDao.add(ticket);
        System.out.println(ticket.getId());
        ShoppingCart shoppingCart = shoppingCartDao.getByUser(user).get();
        List<Ticket> tickets = shoppingCart.getTickets();
        tickets.add(ticket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).get();
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
    }
}
