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

    public static void main(String[] args) throws Exception {
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        Movie movie = new Movie("Fast and Furious");
        movie.setDescription("Action");
        movieService.add(movie);
        CinemaHallService hallService = (CinemaHallService)
                injector.getInstance(CinemaHallService.class);
        CinemaHall hall = new CinemaHall();
        hall.setCapacity(100);
        hall.setDescription("Main Hall");
        hallService.add(hall);
        MovieSession session = new MovieSession();
        session.setCinemaHall(hall);
        session.setMovie(movie);
        session.setShowTime(LocalDateTime.now().plusDays(1));
        ((MovieSessionService) injector.getInstance(MovieSessionService.class)).add(session);
        AuthenticationService authService = (AuthenticationService)
                injector.getInstance(AuthenticationService.class);
        User user = authService.register("bob@gmail.com", "123456");
        ShoppingCartService cartService = (ShoppingCartService)
                injector.getInstance(ShoppingCartService.class);
        cartService.addSession(session, user);
        System.out.println(cartService.getByUser(user));
        cartService.clear(cartService.getByUser(user));
        System.out.println(cartService.getByUser(user));
    }
}
