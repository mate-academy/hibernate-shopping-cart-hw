package mate.academy;

import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    public static final Injector injector = Injector.getInstance("mate.academy");
    private static MovieService movieService =
            (MovieService) injector.getInstance(MovieService.class);
    private static CinemaHallService cinemaHallService =
            (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static MovieSessionService movieSessionService =
            (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static AuthenticationService authenticationService =
            (AuthenticationService) injector.getInstance(AuthenticationService.class);
    private static UserService userService = (UserService) injector.getInstance(UserService.class);
    private static ShoppingCartService shoppingCartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

    public static void main(String[] args) throws AuthenticationException, RegistrationException {
        injectMovie();
        injectCinemaHalls();
        injectMovieSessions();
        authenticationService.register("alexgash8@gmail.com", "123456");
        User user = authenticationService.login("alexgash8@gmail.com", "123456");
        MovieSession movieSession = movieSessionService.get(1L);
        shoppingCartService.addSession(movieSession, user);
        ShoppingCart usersShoppingCart = shoppingCartService.getByUser(user);
        System.out.println("users shopping cart for tomorrow session:" + usersShoppingCart);
        shoppingCartService.clear(usersShoppingCart);
        System.out.println("users shopping cart after clear: " + usersShoppingCart);
    }

    public static void injectMovie() {
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
    }

    public static void injectCinemaHalls() {
        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);
    }

    public static void injectMovieSessions() {
        Movie firstMovie = movieService.get(1L);
        CinemaHall cinemaHallFromDB = cinemaHallService.get(1L);

        MovieSession terminatorMorning =
                new MovieSession(firstMovie, cinemaHallFromDB, LocalDateTime.now());
        MovieSession terminatorDay =
                new MovieSession(firstMovie, cinemaHallFromDB, LocalDateTime.now()
                        .plusHours(3));

        movieSessionService.add(terminatorMorning);
        movieSessionService.add(terminatorDay);
    }

}
