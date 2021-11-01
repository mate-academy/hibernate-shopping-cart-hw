package mate.academy;

import java.time.LocalDateTime;
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
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final MovieService movieService =
            (MovieService) injector.getInstance(MovieService.class);
    private static final CinemaHallService cinemaHallService =
            (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static final MovieSessionService movieSessionService =
            (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static final AuthenticationService authenticationService =
            (AuthenticationService) injector.getInstance(AuthenticationService.class);
    private static final UserService userService =
            (UserService) injector.getInstance(UserService.class);
    private static final ShoppingCartService shoppingCartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

    public static void main(String[] args) {
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");
        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

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

        try {
            authenticationService.register("bob@gmail.com", "bob1234");
            authenticationService.register("alis@gmail.com", "alis1234");
        } catch (RegistrationException e) {
            System.out.println(e.getMessage());
        }

        User userFromDB = userService.findByEmail("bob@gmail.com").get();
        ShoppingCart shoppingCart = shoppingCartService.getByUser(userFromDB);
        System.out.println("Empty Shopping Cart " + shoppingCart);
        System.out.println();
        System.out.println("Add 2 tickets to Session " + tomorrowMovieSession);
        shoppingCartService.addSession(tomorrowMovieSession, userFromDB);
        shoppingCartService.addSession(tomorrowMovieSession, userFromDB);
        shoppingCart = shoppingCartService.getByUser(userFromDB);
        System.out.println("Shopping Cart after added 2 tickets " + shoppingCart);
        System.out.println();

        System.out.println("Clear ShoppingCart ");
        shoppingCartService.clear(shoppingCart);
        shoppingCart = shoppingCartService.getByUser(userFromDB);
        System.out.println("Shopping Cart after clear" + shoppingCart);
        System.out.println();
    }
}
