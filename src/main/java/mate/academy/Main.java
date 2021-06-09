package mate.academy;

import java.time.LocalDateTime;
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
    private static final MovieService movieService
            = (MovieService) injector.getInstance(MovieService.class);
    private static final CinemaHallService cinemaHallService
            = (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static final MovieSessionService movieSessionService
            = (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static final AuthenticationService authenticationService
            = (AuthenticationService) injector.getInstance(AuthenticationService.class);
    private static final ShoppingCartService shoppingCartService
            = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

    public static void main(String[] args) {
        Movie gentlemen = new Movie("The Gentlemen");
        gentlemen.setDescription("BEST MOVIE");
        movieService.add(gentlemen);

        CinemaHall redHall = new CinemaHall();
        redHall.setCapacity(200);
        redHall.setDescription("Very Red");
        cinemaHallService.add(redHall);

        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(gentlemen);
        movieSession.setShowTime(LocalDateTime.now().plusDays(10));
        movieSession.setCinemaHall(redHall);
        movieSessionService.add(movieSession);

        User danylo = authenticationService.register("danylo@gmail.com", "12345");
        shoppingCartService.addSession(movieSession, danylo);
        System.out.println(shoppingCartService.getByUser(danylo));
    }
}
