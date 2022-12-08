package mate.academy;

import java.time.LocalDateTime;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.User;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static MovieService movieService;
    private static CinemaHallService cinemaHallService;
    private static MovieSessionService movieSessionService;
    private static UserService userService;
    private static ShoppingCartService shoppingCartService;

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

        User user = new User();
        user.setEmail("Hello@gmail.com");
        user.setPassword("123445");
        userService.add(user);

        shoppingCartService.registerNewShoppingCart(user);
        shoppingCartService.addSession(tomorrowMovieSession, user);

        System.out.println(shoppingCartService.getByUser(user));

        shoppingCartService.clear(shoppingCartService.getByUser(user));
    }

    private static void serviceInitializer() {
        movieService = (MovieService) injector.getInstance(MovieService.class);
        cinemaHallService
                = (CinemaHallService) injector.getInstance(CinemaHallService.class);
        movieSessionService
                = (MovieSessionService) injector.getInstance(MovieSessionService.class);
        userService = (UserService) injector.getInstance(UserService.class);
        shoppingCartService
                = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
    }
}
