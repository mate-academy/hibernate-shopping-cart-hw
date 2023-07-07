package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

public class Main {
    private static final Injector INJECTOR =
            Injector.getInstance("mate.academy");

    public static void main(String[] args) throws RegistrationException {
        MovieService movieService = (MovieService) INJECTOR.getInstance(MovieService.class);

        Movie fastAndFurious = new Movie("Fast and Furious",
                "An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        CinemaHall firstCinemaHall = new CinemaHall(100,
                "first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall(200,
                "second hall with capacity 200");

        CinemaHallService cinemaHallService =
                (CinemaHallService) INJECTOR.getInstance(CinemaHallService.class);
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = new MovieSession(
                fastAndFurious, firstCinemaHall, LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession(
                fastAndFurious, firstCinemaHall, LocalDateTime.now().minusDays(1L));

        MovieSessionService movieSessionService =
                (MovieSessionService) INJECTOR.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService =
                (AuthenticationService) INJECTOR.getInstance(AuthenticationService.class);
        User user =
                authenticationService.register("firstEmail", "firstPassword");

        ShoppingCartService shoppingCartService =
                (ShoppingCartService) INJECTOR.getInstance(ShoppingCartService.class);
        shoppingCartService.registerNewShoppingCart(user);
        shoppingCartService.addSession(tomorrowMovieSession, user);
        System.out.println(shoppingCartService.getByUser(user));
        shoppingCartService.clear(shoppingCartService.getByUser(user));
        System.out.println(shoppingCartService.getByUser(user));

        User anotherUser =
                authenticationService.register("secondEmail", "secondPassword");
        shoppingCartService.registerNewShoppingCart(anotherUser);
        System.out.println(shoppingCartService.getByUser(anotherUser));
        shoppingCartService.addSession(yesterdayMovieSession, anotherUser);

    }
}
