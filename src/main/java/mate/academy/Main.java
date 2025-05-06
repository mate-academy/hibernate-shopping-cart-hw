package mate.academy;

import java.time.LocalDateTime;
import mate.academy.lib.Injector;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        User user = new User();
        user.setId(1L); // Use actual ID if persisted, or persist here
        user.setEmail("test@example.com");
        MovieSession movieSession = new MovieSession();
        movieSession.setId(1L); // Use actual ID if persisted, or persist here
        movieSession.setShowTime(LocalDateTime.now().plusDays(1));
        ShoppingCartService shoppingCartService = (ShoppingCartService)
                injector.getInstance(ShoppingCartService.class);
        shoppingCartService.registerNewShoppingCart(user);
        System.out.println("New shopping cart created for user: " + user.getEmail());
        shoppingCartService.addSession(movieSession, user);
        System.out.println("Movie session added to cart.");
        ShoppingCart cart = shoppingCartService.getByUser(user);
        System.out.println("Tickets in cart: " + cart.getTickets().size());
        shoppingCartService.clear(cart);
        System.out.println("Shopping cart cleared.");
        ShoppingCart clearedCart = shoppingCartService.getByUser(user);
        System.out.println("Tickets in cart after clearing: " + clearedCart.getTickets().size());
    }
}
