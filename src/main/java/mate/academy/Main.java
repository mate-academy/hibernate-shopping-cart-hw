package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
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

public class Main {
    private static final Injector injector
            = Injector.getInstance("mate.academy");
    private static final MovieService movieService
            = (MovieService) injector.getInstance(MovieService.class);
    private static final CinemaHallService cinemaHallService
            = (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static final MovieSessionService movieSessionService
            = (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static final ShoppingCartService shoppingCartService
            = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
    private static final AuthenticationService authenticationService
            = (AuthenticationService) injector.getInstance(AuthenticationService.class);
    private static final UserService userService
            = (UserService) injector.getInstance(UserService.class);
    private static final String COLOR_ANSI_BEGIN = "\033[32;40m";
    private static final String COLOR_ANSI_END = "\033[0m";

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

        User user = new User();
        user.setEmail("test1@gmail.com");
        user.setPassword("1111");
        try {
            authenticationService.register(user.getEmail(), user.getPassword());
            user = authenticationService.login(user.getEmail(), user.getPassword());
        } catch (RegistrationException | AuthenticationException e) {
            throw new RuntimeException("Can't login or register user" + user, e);
        }

        System.out.println(COLOR_ANSI_BEGIN + shoppingCartService.getByUser(user) + COLOR_ANSI_END);

        shoppingCartService.addSession(movieSessionService.get(1L), user);
        shoppingCartService.addSession(movieSessionService.get(2L), user);
        System.out.println(COLOR_ANSI_BEGIN + shoppingCartService.getByUser(user) + COLOR_ANSI_END);

        shoppingCartService.clear(shoppingCartService.getByUser(user));
        System.out.println(COLOR_ANSI_BEGIN + shoppingCartService.getByUser(user) + COLOR_ANSI_END);

    }
}
