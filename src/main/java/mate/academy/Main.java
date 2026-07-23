package mate.academy;

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
        final UserService userService = (UserService) injector.getInstance(UserService.class);
        final ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        final MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        final MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        final CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);

        User bob = new User();
        bob.setEmail("bob@bob.com");
        bob.setPassword("bob");
        userService.add(bob);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(bob);
        shoppingCartService.registerNewShoppingCart(bob);

        Movie movie = new Movie();
        movie.setTitle("Monk");
        movie.setDescription("Detective movie");
        movieService.add(movie);

        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(100);
        cinemaHall.setDescription("CinemaCity");
        cinemaHallService.add(cinemaHall);

        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(movie);
        movieSession.setCinemaHall(cinemaHall);
        movieSessionService.add(movieSession);

        shoppingCartService.addSession(movieSessionService.get(1L), bob);
    }
}
