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

public class Main {
    private static final Injector injector = Injector
            .getInstance("mate.academy");

    public static void main(String[] args) {
        final MovieService movieService = (MovieService) injector
                .getInstance(MovieService.class);
        final CinemaHallService cinemaHallService
                = (CinemaHallService) injector.getInstance(CinemaHallService.class);
        final MovieSessionService movieSessionService
                = (MovieSessionService) injector.getInstance(MovieSessionService.class);
        final ShoppingCartService shoppingCartService
                = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        final AuthenticationService authService
                = (AuthenticationService) injector.getInstance(AuthenticationService.class);

        User user = null;
        try {
            user = authService.register("john@example.com", "1234");
        } catch (RegistrationException e) {
            System.out.println("Registration failed: "
                    + e.getMessage());
            return;
        }

        Movie movie = new Movie("Fast and Furious");
        movie.setDescription("An action film about street racing.");
        movieService.add(movie);

        CinemaHall hall = new CinemaHall();
        hall.setCapacity(100);
        hall.setDescription("Main Hall");
        cinemaHallService.add(hall);

        MovieSession session = new MovieSession();
        session.setMovie(movie);
        session.setCinemaHall(hall);
        session.setShowTime(LocalDateTime.now().plusDays(1));
        movieSessionService.add(session);

        shoppingCartService.registerNewShoppingCart(user);
        shoppingCartService.addSession(session, user);

        ShoppingCart cart = shoppingCartService.getByUser(user);
        System.out.println("Cart after adding ticket: "
                + cart);

        shoppingCartService.clear(cart);
        System.out.println("Cart after clearing: "
                + shoppingCartService.getByUser(user));
    }
}
