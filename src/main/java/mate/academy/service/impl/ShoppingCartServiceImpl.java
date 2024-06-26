package mate.academy.service.impl;

import mate.academy.dao.*;
import mate.academy.exception.*;
import mate.academy.lib.*;
import mate.academy.model.*;
import mate.academy.service.*;

import java.util.*;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    /**
     * This method is responsible for adding a Ticket to the ShoppingCart
     * @param movieSession contains the information required for the ticket
     * @param user - the User who wants to buy the ticket for a specific movieSession
     */
    @Inject
    ShoppingCartDao shoppingCartDao;
    @Inject
    TicketDao ticketDao;
    @Override
    public void addSession(MovieSession movieSession, User user) {
        ShoppingCart shoppingCart = shoppingCartDao.getByUser(user)
                .orElseThrow(() -> new DataProcessingException("Shopping cart is empty! (add session method)"));

        Ticket ticket = new Ticket(movieSession, user);
        shoppingCart.getTickets().add(ticket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user)
                .orElseThrow(() -> new DataProcessingException("TODO"));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        shoppingCartDao.add(new ShoppingCart(List.of(), user));
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);
    }
}
