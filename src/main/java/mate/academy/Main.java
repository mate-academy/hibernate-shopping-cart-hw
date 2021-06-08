package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    public static final Logger logger = LogManager.getLogger(Main.class);
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final MovieService movieService = (MovieService)
            injector.getInstance(MovieService.class);
    private static final CinemaHallService cinemaHallService = (CinemaHallService)
            injector.getInstance(CinemaHallService.class);
    private static final MovieSessionService movieSessionService = (MovieSessionService)
            injector.getInstance(MovieSessionService.class);
    private static final UserService userService = (UserService)
            injector.getInstance(UserService.class);
    private static final AuthenticationService authenticationService = (AuthenticationService)
            injector.getInstance(AuthenticationService.class);
    private static final ShoppingCartService shoppingCartService = (ShoppingCartService)
            injector.getInstance(ShoppingCartService.class);

    public static void main(String[] args) {
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        System.out.println("--------user service--------");
        System.out.println(authenticationService.register("alice@gmail.com", "1234"));
        System.out.println(authenticationService.register("bob@gmail.com", "bob1234"));
        User bob = userService.findByEmail("bob@gmail.com").get();
        User alice = userService.findByEmail("alice@gmail.com").get();
        System.out.println(bob);
        System.out.println(alice);
        try {
            System.out.println(authenticationService.login("a_lice@gmail.com", "1234"));
        } catch (AuthenticationException e) {
            logger.error("Authentication was unsuccessful: incorrect email", e);
        }
        try {
            System.out.println(authenticationService.login("alice@gmail.com", "11111"));
        } catch (AuthenticationException e) {
            logger.error("Authentication was unsuccessful: incorrect password", e);
        }
        try {
            System.out.println(authenticationService.register("alice@gmail.com", "11111"));
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }

        System.out.println("------shopping cart service------");
        System.out.println(shoppingCartService.getByUser(bob));
        shoppingCartService.addSession(tomorrowMovieSession, bob);
        shoppingCartService.addSession(yesterdayMovieSession, bob);
        System.out.println(shoppingCartService.getByUser(bob));
        shoppingCartService.clear(shoppingCartService.getByUser(bob));
        System.out.println(shoppingCartService.getByUser(bob));
    }
}
