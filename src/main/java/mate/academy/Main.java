package mate.academy;

import java.time.LocalDate;
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

    private static final String bobEmail = "bob@gmail.com";
    private static final String bobPassword = "qwerty";
    private static final String aliceEmail = "alice@gmail.com";
    private static final String alicePassword = "1q2w3e";
    private static final String johnEmail = "john@gmail.com";
    private static final String johnPassword = "qqwweerr";
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {

        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);

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

        //add users
        AuthenticationService authenticationService = (AuthenticationService)
                injector.getInstance(AuthenticationService.class);
        try {
            authenticationService.register(bobEmail, bobPassword);
            authenticationService.register(aliceEmail, alicePassword);
            authenticationService.register(johnEmail, johnPassword);
        } catch (RegistrationException e) {
            throw new RuntimeException(e);
        }

        // login user
        User bob;
        User alice;
        User john;
        try {
            bob = authenticationService.login(bobEmail, bobPassword);
            alice = authenticationService.login(aliceEmail, alicePassword);
            john = authenticationService.login(johnEmail, johnPassword);
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }

        // add session
        ShoppingCartService shoppingCartService = (ShoppingCartService)
                injector.getInstance(ShoppingCartService.class);

        shoppingCartService.addSession(tomorrowMovieSession, bob);
        shoppingCartService.addSession(tomorrowMovieSession, alice);
        shoppingCartService.addSession(tomorrowMovieSession, alice);
        shoppingCartService.addSession(tomorrowMovieSession, john);

        shoppingCartService.addSession(yesterdayMovieSession, bob);
        shoppingCartService.addSession(yesterdayMovieSession, bob);
        shoppingCartService.addSession(yesterdayMovieSession, alice);
        shoppingCartService.addSession(yesterdayMovieSession, alice);
        shoppingCartService.addSession(yesterdayMovieSession, alice);
        shoppingCartService.addSession(yesterdayMovieSession, john);

        System.out.println("------------ Test getByUser method for all users ------------");
        System.out.println(shoppingCartService.getByUser(bob));
        System.out.println(shoppingCartService.getByUser(alice));
        System.out.println(shoppingCartService.getByUser(john));

        System.out.println("------------ Clear shopping carts ------------");
        System.out.println("Before clearing");
        System.out.println(shoppingCartService.getByUser(alice));
        shoppingCartService.clear(shoppingCartService.getByUser(alice));
        System.out.println("After clearing");
        System.out.println(shoppingCartService.getByUser(alice));
    }
}
