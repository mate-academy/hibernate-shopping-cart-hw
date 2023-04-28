package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
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

    @Override
    public void addTicket(MovieSession movieSession, User user) {
        Optional<ShoppingCart> cart = shoppingCartDao.getByUser(user);
        if (cart.isPresent()) {
            ShoppingCart shoppingCart = cart.get();
            shoppingCart.getTickets().add(new Ticket(movieSession, user));
            shoppingCartDao.update(shoppingCart);
        } else {
            ShoppingCart shoppingCart = new ShoppingCart(user);
            shoppingCart.getTickets().add(new Ticket(movieSession, user));
            shoppingCartDao.add(shoppingCart);
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user).get();
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart newShoppingCart = new ShoppingCart(user);
        shoppingCartDao.add(newShoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);
    }
}
