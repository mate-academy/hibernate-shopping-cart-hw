package mate.academy;

import mate.academy.lib.Injector;
import mate.academy.model.MovieSession;
import mate.academy.model.User;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");

        MovieService movieService =
                (MovieService) injector.getInstance(MovieService.class);
        CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        UserService userService =
                (UserService) injector.getInstance(UserService.class);
        User user = new User();
        user.setEmail("artemnurmetow@gmail.com");
        user.setPassword("qwerty");
        userService.add(user);
        MovieSession movieSession = new MovieSession();
        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(movieSession);
        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        shoppingCartService.registerNewShoppingCart(user);
        shoppingCartService.addSession(movieSession,user);
    }
}
