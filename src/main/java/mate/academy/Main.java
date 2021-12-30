package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
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
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        MovieService movieService =
                (MovieService) injector.getInstance(MovieService.class);
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

        CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        User alice = new User();
        alice.setEmail("bobovich@gmail.com");
        alice.setPassword("1222d");
        User bob = new User();
        bob.setEmail("alice@gmail.com");
        bob.setPassword("3dd3");
        try {
            authenticationService.register(bob.getEmail(), bob.getPassword());
            authenticationService.register(alice.getEmail(), alice.getPassword());
        } catch (RegistrationException exception) {
            throw new RuntimeException(exception);
        }
        try {
            alice = authenticationService.login("bobovich@gmail.com", "1222d");
            bob = authenticationService.login("alice@gmail.com", "3dd3");
        } catch (AuthenticationException exception) {
            throw new RuntimeException(exception);
        }
        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        shoppingCartService.addSession(tomorrowMovieSession, alice);
        shoppingCartService.addSession(yesterdayMovieSession, bob);
        ShoppingCart shoppingCartByBob = shoppingCartService.getByUser(bob);
        ShoppingCart shoppingCartByAlice = shoppingCartService.getByUser(alice);
        System.out.println("Bob's shopping cart is " + shoppingCartByBob);
        System.out.println("Alice's shopping cart is " + shoppingCartByAlice);
        shoppingCartService.clear(shoppingCartByAlice);
        System.out.println("Alice shopping cart after clearing : " + shoppingCartByAlice);
        shoppingCartService.clear(shoppingCartByBob);
        System.out.println("Bob shopping cart after clearing : " + shoppingCartByBob);
    }
}
