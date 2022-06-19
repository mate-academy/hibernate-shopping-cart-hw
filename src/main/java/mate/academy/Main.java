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

public class Main {
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        // movies
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        Movie cyborgs = new Movie("Cyborgs");
        cyborgs.setDescription("Story about russian-Ukrainian war.");
        movieService.add(cyborgs);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);
        // cinema halls
        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");
        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));
        // movie sessions
        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(secondCinemaHall);
        yesterdayMovieSession.setMovie(cyborgs);
        yesterdayMovieSession.setShowTime(LocalDateTime.now());

        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                cyborgs.getId(), LocalDate.now()));
        // user
        AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        User testUserFromDb = null;
        try {
            authenticationService.register("user@gmail.com", "testPassword");
            testUserFromDb = authenticationService.login("user@gmail.com", "testPassword");
        } catch (RegistrationException e) {
            throw new RuntimeException("Couldn`t register test user");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Couldn`t login test user");
        }
        // shopping cart and ticket
        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        System.out.println(shoppingCartService.getByUser(testUserFromDb));
        shoppingCartService.addSession(tomorrowMovieSession, testUserFromDb);
        shoppingCartService.addSession(yesterdayMovieSession, testUserFromDb);
        System.out.println(shoppingCartService.getByUser(testUserFromDb));
        //shoppingCartService.clear(shoppingCartService.getByUser(testUserFromDb));
        System.out.println(shoppingCartService.getByUser(testUserFromDb));
    }
}
