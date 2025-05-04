package mate.academy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
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
    @Transactional
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket(movieSession, user);
        ticketDao.add(ticket);
        ShoppingCart userShCart = getByUser(user);
        userShCart.getTickets().add(ticket);
        shoppingCartDao.update(userShCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        Optional<ShoppingCart> optionalShoppingCart = Optional
                .ofNullable(shoppingCartDao.getByUser(user));
        return optionalShoppingCart.orElseThrow(() ->
                new EntityNotFoundException("Shopping cart not found for user "
                        + user.getId()));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        List<Ticket> tickets = new ArrayList<>();
        shoppingCartDao.add(new ShoppingCart(user, tickets));
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);
    }
}
