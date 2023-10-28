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

public class Main {
    private static final Injector INJECTOR_MAP = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        MovieService movieService =
                (MovieService) INJECTOR_MAP.getInstance(MovieService.class);

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
                (CinemaHallService) INJECTOR_MAP.getInstance(CinemaHallService.class);
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
                (MovieSessionService) INJECTOR_MAP.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService =
                (AuthenticationService) INJECTOR_MAP.getInstance(AuthenticationService.class);
        ShoppingCartService shoppingCartService =
                (ShoppingCartService) INJECTOR_MAP.getInstance(ShoppingCartService.class);
        try {
            User bob = authenticationService.register("bob@email.com", "querty");
            shoppingCartService.addSession(tomorrowMovieSession, bob);
            shoppingCartService.addSession(tomorrowMovieSession, bob);
            shoppingCartService.addSession(tomorrowMovieSession, bob);
            System.out.println("\n\nShopping cart user " + bob + "\n\n"
                    + shoppingCartService.getByUser(bob));
        } catch (RegistrationException e) {
            System.out.println(e);
        }

        try {
            User alice = authenticationService.register("alice@email.com", "querty");
            shoppingCartService.addSession(tomorrowMovieSession, alice);
            shoppingCartService.addSession(tomorrowMovieSession, alice);
            shoppingCartService.addSession(tomorrowMovieSession, alice);
            shoppingCartService.addSession(tomorrowMovieSession, alice);
            shoppingCartService.addSession(tomorrowMovieSession, alice);
            System.out.println("\n\nShopping cart user " + alice + "\n\n"
                    + shoppingCartService.getByUser(alice));
        } catch (RegistrationException e) {
            System.out.println(e);
        }

        try {
            User del = authenticationService.register("del@email.com", "querty");
            shoppingCartService.addSession(tomorrowMovieSession, del);
            System.out.println("\n\nShopping cart user " + del
                    + "\n\nbefore clearing - " + shoppingCartService.getByUser(del));
            shoppingCartService.clear(shoppingCartService.getByUser(del));
            System.out.println("\n\nShopping cart user " + del
                    + "\n\nafter clearing - " + shoppingCartService.getByUser(del));
        } catch (RegistrationException e) {
            System.out.println(e);
        }
    }
}
