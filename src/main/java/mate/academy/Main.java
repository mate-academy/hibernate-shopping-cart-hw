package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegisterException;
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
    private static final Injector inject = Injector.getInstance("mate.academy");

    public static void main(String[] args) throws RegisterException {
        MovieService movieService = (MovieService)
                inject.getInstance(MovieService.class);

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
                inject.getInstance(CinemaHallService.class);
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
                inject.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        User user1 = new User();
        user1.setPassword("12_TrY");
        user1.setEmail("qwqeqwlnmail.com");

        AuthenticationService authenticationService = (AuthenticationService)
                inject.getInstance(AuthenticationService.class);
        try {
            user1 = authenticationService.register(user1.getEmail(), user1.getPassword());
        } catch (RegisterException e) {
            throw new RegisterException("User already exists in the DB ");
        }
        try {
            user1 = authenticationService.login(user1.getEmail(), user1.getPassword());
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }

        ShoppingCartService shoppingCartService = (ShoppingCartService)
                inject.getInstance(ShoppingCartService.class);
        shoppingCartService.addSession(yesterdayMovieSession, user1);
        ShoppingCart shoppingCart = shoppingCartService.getByUser(user1);
        System.out.println(shoppingCart);

        shoppingCartService.clear(shoppingCart);
        System.out.println(shoppingCartService.getByUser(user1));
    }
}
