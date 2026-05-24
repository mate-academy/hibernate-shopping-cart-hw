package mate.academy;

import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.security.AuthenticationServiceImpl;
import mate.academy.service.ShoppingCartService;

public class Main {
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");
        ShoppingCartService shoppingCartService = (ShoppingCartService)
                injector.getInstance(ShoppingCartService.class);
        AuthenticationService authenticationService = (AuthenticationServiceImpl)
                injector.getInstance(AuthenticationService.class);

        MovieSession firstMovieSession = new MovieSession();

        User firstUser = null;
        try {
            firstUser = authenticationService.register("marek@interia.pl", "Haslo");
        } catch (RegistrationException e) {
            throw new RuntimeException(e);
        }

        shoppingCartService.addSession(firstMovieSession, firstUser);
        ShoppingCart firstShoppingCart = shoppingCartService.getByUser(firstUser);
        System.out.println(firstShoppingCart);
        shoppingCartService.clear(firstShoppingCart);
        System.out.println(firstShoppingCart);

    }
}
