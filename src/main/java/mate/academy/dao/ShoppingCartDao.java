package mate.academy.dao;

import java.util.Optional;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;

public interface ShoppingCartDao {
    void addTicket(Ticket ticket);

    Optional<ShoppingCart> getByUser(User user);

    void update(ShoppingCart cart);

    void register(ShoppingCart cart);

    void clear(ShoppingCart shoppingCart);
}
