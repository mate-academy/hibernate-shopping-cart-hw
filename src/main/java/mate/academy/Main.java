package mate.academy;

import java.time.LocalDateTime;
import java.util.Optional;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    private static final Injector injects = Injector.getInstance("mate.academy");

    public static void main(String[] args) throws Exception {
        final MovieService movieService = (MovieService) injects.getInstance(MovieService.class);
        final CinemaHallService cinemaHallService =
                (CinemaHallService) injects.getInstance(CinemaHallService.class);
        final MovieSessionService movieSessionService =
                (MovieSessionService) injects.getInstance(MovieSessionService.class);
        final UserService userService = (UserService) injects.getInstance(UserService.class);
        final ShoppingCartService cartService =
                (ShoppingCartService) injects.getInstance(ShoppingCartService.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);

        CinemaHall firstHall = new CinemaHall();
        firstHall.setCapacity(100);
        firstHall.setDescription("first hall with capacity 100");
        cinemaHallService.add(firstHall);

        MovieSession tomorrowSession = new MovieSession();
        tomorrowSession.setMovie(fastAndFurious);
        tomorrowSession.setCinemaHall(firstHall);
        tomorrowSession.setShowTime(LocalDateTime.now().plusDays(1));
        movieSessionService.add(tomorrowSession);

        MovieSession yesterdaySession = new MovieSession();
        yesterdaySession.setMovie(fastAndFurious);
        yesterdaySession.setCinemaHall(firstHall);
        yesterdaySession.setShowTime(LocalDateTime.now().minusDays(1));
        movieSessionService.add(yesterdaySession);

        Optional<User> optionalUser = userService.findByEmail("qwerty");
        User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found"));

        cartService.registerNewShoppingCart(user);

        cartService.addSession(tomorrowSession, user);

        ShoppingCart cart = cartService.getByUser(user);
        System.out.println(cart);

        cartService.clear(cart);
        System.out.println("ShoppingCart після очищення: " + cartService.getByUser(user));
    }
}
