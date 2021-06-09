package mate.academy;

import java.time.LocalDateTime;
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
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final ShoppingCartService shoppingCartService = (ShoppingCartService)
            injector.getInstance(ShoppingCartService.class);
    private static final AuthenticationService authenticationService = (AuthenticationService)
            injector.getInstance(AuthenticationService.class);
    private static final MovieService movieService = (MovieService)
            injector.getInstance(MovieService.class);
    private static final CinemaHallService cinemaHallService = (CinemaHallService)
            injector.getInstance(CinemaHallService.class);
    private static final MovieSessionService movieSessionService = (MovieSessionService)
            injector.getInstance(MovieSessionService.class);

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

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));
        movieSessionService.add(yesterdayMovieSession);

        User jerry = null;
        try {
            jerry = authenticationService.register("MMM@gmail.com", "12345");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        shoppingCartService.addSession(tomorrowMovieSession, jerry);
        shoppingCartService.addSession(yesterdayMovieSession, jerry);
        ShoppingCart jerryShoppingCart = shoppingCartService.getByUser(jerry);
        System.out.println(jerryShoppingCart);
        shoppingCartService.clear(jerryShoppingCart);
        ShoppingCart updatedJerryShoppingCart = shoppingCartService.getByUser(jerry);
        System.out.println(updatedJerryShoppingCart);
    }
}
