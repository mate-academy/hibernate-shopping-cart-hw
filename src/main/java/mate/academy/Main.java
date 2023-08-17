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
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final MovieService movieService =
            (MovieService) injector.getInstance(MovieService.class);
    private static final CinemaHallService hallService =
            (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static final MovieSessionService sessionService =
            (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static final AuthenticationService authenticationService =
            (AuthenticationService) injector.getInstance(AuthenticationService.class);
    private static final ShoppingCartService cartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

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

        hallService.add(firstCinemaHall);
        hallService.add(secondCinemaHall);

        System.out.println(hallService.getAll());
        System.out.println(hallService.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        sessionService.add(tomorrowMovieSession);
        sessionService.add(yesterdayMovieSession);

        System.out.println(sessionService.get(yesterdayMovieSession.getId()));
        System.out.println(sessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        User bob = null;
        User jack = null;
        try {
            bob = authenticationService.register("bob@gmail.com", "hardPassword");
            jack = authenticationService.register("jack@gmail.com", "Password");
        } catch (RegistrationException e) {
            System.out.println(e.getMessage());
        }

        cartService.addSession(tomorrowMovieSession, bob);
        cartService.addSession(tomorrowMovieSession, jack);

        System.out.println(cartService.getByUser(bob));
        System.out.println(cartService.getByUser(jack));
    }
}
