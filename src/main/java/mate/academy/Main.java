package mate.academy;

import mate.academy.lib.Injector;
import mate.academy.model.MovieSession;
import mate.academy.model.User;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    private static final Injector INJECTOR = Injector.getInstance("mate.academy");
    private static MovieSessionService movieSessionService = (MovieSessionService) INJECTOR
            .getInstance(MovieSessionService.class);
    private static UserService userService = (UserService) INJECTOR
            .getInstance(UserService.class);
    private static ShoppingCartService shoppingCartService = (ShoppingCartService) INJECTOR
            .getInstance(ShoppingCartService.class);

    public static void main(String[] args) {
        User user = new User();
        user.setEmail("alice_mail");
        user.setPassword("qwr123");
        userService.add(user);
        shoppingCartService.registerNewShoppingCart(userService.findByEmail("alice_mail").get());
        User bob = userService.findByEmail("bobemail").get();
        System.out.println("OK");
        MovieSession movieSession = movieSessionService.get(1L);
        System.out.println("no");
        shoppingCartService.addSession(movieSession, bob);
    }
}
