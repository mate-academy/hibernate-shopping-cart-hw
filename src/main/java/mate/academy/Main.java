package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import mate.academy.lib.Injector;
import mate.academy.model.*;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;

public class Main {
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");

        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        User user = new User();
        MovieSession movieSession = new MovieSession();

        shoppingCartService.addSession(movieSession, user);

        ShoppingCart userShoppingCart = shoppingCartService.getByUser(user);
        if (userShoppingCart != null) {
            System.out.println("User's shopping cart ID: " + userShoppingCart.getId());
        } else {
            System.out.println("User does not have a shopping cart");
        }

        shoppingCartService.registerNewShoppingCart(user);
        ShoppingCart newUserShoppingCart = shoppingCartService.getByUser(user);
        System.out.println("New User's shopping cart ID: " + newUserShoppingCart.getId());

        shoppingCartService.clear(newUserShoppingCart);
        System.out.println("Shopping Cart is cleared");
    }
}
