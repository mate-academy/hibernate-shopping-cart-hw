package mate.academy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
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
        Optional<ShoppingCart> shoppingCartByUserOptional = shoppingCartDao.getByUser(user);
        if (shoppingCartByUserOptional.isPresent()) {
            Ticket newTicket = new Ticket();
            newTicket.setMovieSession(movieSession);
            newTicket.setUser(user);
            ticketDao.add(newTicket);
            ShoppingCart shoppingCartByUser = shoppingCartByUserOptional.get();
            List<Ticket> tickets = shoppingCartByUser.getTickets();
            tickets.add(newTicket);
            shoppingCartByUser.setTickets(tickets);
            shoppingCartDao.update(shoppingCartByUser);
        } else {
            throw new EntityNotFoundException("No shopping cart was found by user: "
                    + user);
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        Optional<ShoppingCart> shoppingCartByUserOptional = shoppingCartDao.getByUser(user);
        if (shoppingCartByUserOptional.isPresent()) {
            return shoppingCartByUserOptional.get();
        }
        throw new EntityNotFoundException("There is no shopping cart by user: "
                                            + user);
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
        shoppingCart.setTickets(Collections.emptyList());
        shoppingCartDao.update(shoppingCart);
    }
}
