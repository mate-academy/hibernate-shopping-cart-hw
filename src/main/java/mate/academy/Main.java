package mate.academy;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    public static void main(String[] args) throws RegistrationException, AuthenticationException {
        Injector injector = Injector.getInstance("mate.academy");
        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        UserService userService = (UserService) injector.getInstance(UserService.class);

        Optional<User> userFromDb = userService.findByEmail("user@gmail.com");
        MovieSession movieSessionFromDb = movieSessionService.get(1L);

        shoppingCartService.registerNewShoppingCart(userFromDb.get());
        shoppingCartService.addSession(movieSessionFromDb, userFromDb.get());
        ShoppingCart cart = shoppingCartService.getByUser(userFromDb.get());
        System.out.println("Tickets in cart: " + cart.getTickets().size());
        shoppingCartService.clear(cart);
        System.out.println("Tickets after clear: " + shoppingCartService
                .getByUser(userFromDb.get()).getTickets().size());
    }
}
