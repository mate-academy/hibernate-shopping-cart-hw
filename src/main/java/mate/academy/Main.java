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
import mate.academy.util.HashUtil;

public class Main {
    private static final Injector INJECTOR = Injector.getInstance("mate.academy");
    private static final ShoppingCartService SHOPPING_CART_SERVICE = (ShoppingCartService)
            INJECTOR.getInstance(ShoppingCartService.class);
    private static final MovieService MOVIE_SERVICE =
            (MovieService) INJECTOR.getInstance(MovieService.class);
    private static final CinemaHallService CINEMA_HALL_SERVICE =
            (CinemaHallService) INJECTOR.getInstance(CinemaHallService.class);
    private static final UserService USER_SERVICE =
            (UserService) INJECTOR.getInstance(UserService.class);

    public static void main(String[] args) {
        User user = new User();
        user.setEmail("email@gmail.com");
        user.setSalt(HashUtil.getSalt());
        user.setPassword(HashUtil.hashPassword("correct pass", user.getSalt()));
        USER_SERVICE.add(user);

        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(12);
        cinemaHall.setDescription("Modern hall");
        CINEMA_HALL_SERVICE.add(cinemaHall);

        Movie movie = new Movie();
        movie.setTitle("New movie");
        movie.setDescription("Something realy new");
        MOVIE_SERVICE.add(movie);

        MovieSession movieSession = new MovieSession();
        movieSession.setShowTime(LocalDateTime.now());
        movieSession.setMovie(movie);
        movieSession.setCinemaHall(cinemaHall);
        MovieSessionService movieSessionService = (MovieSessionService)
                INJECTOR.getInstance(MovieSessionService.class);
        movieSessionService.add(movieSession);

        SHOPPING_CART_SERVICE.registerNewShoppingCart(user);
        SHOPPING_CART_SERVICE.addSession(movieSession, user);
        ShoppingCart shoppingCart = SHOPPING_CART_SERVICE.getByUser(user);
        System.out.println("\n\n");
        System.out.println(shoppingCart);
        System.out.println("\n\n");
        SHOPPING_CART_SERVICE.clear(shoppingCart);
        System.out.println("\n\n");
        System.out.println(SHOPPING_CART_SERVICE.getByUser(user));
        System.out.println("\n\n");

    }
}
