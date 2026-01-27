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
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");
        final ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        final UserService userService =
                (UserService) injector.getInstance(UserService.class);
        final MovieService movieService =
                (MovieService) injector.getInstance(MovieService.class);
        final CinemaHallService hallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        final MovieSessionService sessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);

        User user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("password");
        userService.add(user);

        shoppingCartService.registerNewShoppingCart(user);

        Movie movie = new Movie();
        movie.setDescription("movieDescription");
        movieService.add(movie);

        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(50);
        cinemaHall.setDescription("cinemaDescription");
        hallService.add(cinemaHall);

        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(movie);
        movieSession.setCinemaHall(cinemaHall);
        movieSession.setShowTime(LocalDateTime.now().plusDays(1));
        sessionService.add(movieSession);

        shoppingCartService.addSession(movieSession, user);
        ShoppingCart cart = shoppingCartService.getByUser(user);
        System.out.println("Cart with tickets: " + cart);
        shoppingCartService.clear(cart);
        System.out.println("Cart after clear: " + shoppingCartService.getByUser(user));
    }
}
