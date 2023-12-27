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
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final String LOGIN_BOB = "Bob@gmail.com";
    private static final String PASSWORD_BOB = "Bob1234";
    private static final String LOGIN_ALICE = "Alice@gmail.com";
    private static final String PASSWORD_ALICE = "Alice";

    public static void main(String[] args) {
        MovieService movieService = (MovieService)
                injector.getInstance(MovieService.class);

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

        CinemaHallService cinemaHallService = (CinemaHallService)
                injector.getInstance(CinemaHallService.class);
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

        MovieSessionService movieSessionService = (MovieSessionService)
                injector.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService = (AuthenticationService)
                injector.getInstance(AuthenticationService.class);
        try {
            System.out.println("Register new user Bob!");
            authenticationService.register(LOGIN_BOB, PASSWORD_BOB);
            System.out.println("Register new user Alice!");
            authenticationService.register(LOGIN_ALICE, PASSWORD_ALICE);
        } catch (RegistrationException e) {
            throw new RuntimeException("This email is already registered. Try the new one.");
        }

        UserService userService = (UserService) injector.getInstance(UserService.class);
        System.out.println("Get registered user Bob by login!");
        User userBob = userService.findByEmail(LOGIN_BOB).orElse(null);
        System.out.println(userBob);
        System.out.println("Get registered user Alice by login!");
        User userAlice = userService.findByEmail(LOGIN_ALICE).orElse(null);
        System.out.println(userAlice);

        ShoppingCartService shoppingCartService = (ShoppingCartService)
                injector.getInstance(ShoppingCartService.class);
        System.out.println("Check if Bob's shopping cart exists after registration!");
        System.out.println(shoppingCartService.getByUser(userBob));
        System.out.println("Check if Alice's shopping cart exists after registration!");
        System.out.println(shoppingCartService.getByUser(userAlice));
        System.out.println("Add movie session (->ticket) for Bob!");
        shoppingCartService.addSession(tomorrowMovieSession, userBob);
        System.out.println("Get shopping cart for user Bob with a ticket!");
        System.out.println(shoppingCartService.getByUser(userBob));
        System.out.println("Clear Bob's shopping cart!");
        shoppingCartService.clear(shoppingCartService.getByUser(userBob));
        System.out.println("Check if Bob's shopping cart is empty after clearing!");
        System.out.println(shoppingCartService.getByUser(userBob));
    }
}
