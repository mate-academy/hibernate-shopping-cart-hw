package mate.academy;

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
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static MovieService movieService;
    private static CinemaHallService cinemaHallService;
    private static MovieSessionService movieSessionService;
    private static ShoppingCartService shoppingCartService;
    private static AuthenticationService authenticationService;

    static {
        serviceInitializer();
    }

    public static void main(String[] args) {

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");
        cinemaHallService.add(firstCinemaHall);

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));
        movieSessionService.add(tomorrowMovieSession);

        User maxine = null;
        try {
            authenticationService.register("maxine@gmail.com", "123445");
        } catch (RegistrationException e) {
            throw new RuntimeException("Can't register new user", e);
        }

        try {
            maxine = authenticationService.login("maxine@gmail.com", "123445");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Can't login by email=maxine@gmail.com", e);
        }

        shoppingCartService.addSession(tomorrowMovieSession, maxine);

        System.out.println(shoppingCartService.getByUser(maxine));

        shoppingCartService.clear(shoppingCartService.getByUser(maxine));

        System.out.println(shoppingCartService.getByUser(maxine));
    }

    private static void serviceInitializer() {
        movieService = (MovieService) injector.getInstance(MovieService.class);
        cinemaHallService
                = (CinemaHallService) injector.getInstance(CinemaHallService.class);
        movieSessionService
                = (MovieSessionService) injector.getInstance(MovieSessionService.class);
        shoppingCartService
                = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        authenticationService
                = (AuthenticationService) injector.getInstance(AuthenticationService.class);
    }
}
