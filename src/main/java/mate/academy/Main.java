package mate.academy;

import java.time.LocalDateTime;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        Movie movie = new Movie("Fast and Furious");
        movie.setDescription("Action movie");

        MovieService movieService =
                (MovieService) injector.getInstance(MovieService.class);
        movieService.add(movie);

        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(100);
        cinemaHall.setDescription("Main hall");

        CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        cinemaHallService.add(cinemaHall);

        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(movie);
        movieSession.setCinemaHall(cinemaHall);
        movieSession.setShowTime(LocalDateTime.now().plusDays(1));

        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(movieSession);

        User user = new User();
        user.setEmail("weronika" + System.currentTimeMillis() + "@cinema.com");
        user.setPassword("1234");

        UserService userService =
                (UserService) injector.getInstance(UserService.class);
        userService.add(user);

        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        shoppingCartService.registerNewShoppingCart(user);
        shoppingCartService.addSession(movieSession, user);

        ShoppingCart cart = shoppingCartService.getByUser(user);

        System.out.println("SHOPPING CART:");
        System.out.println(cart);

        shoppingCartService.clear(cart);

        System.out.println("CART AFTER CLEAR:");
        System.out.println(shoppingCartService.getByUser(user));
    }
}
