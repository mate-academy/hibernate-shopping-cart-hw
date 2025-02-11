package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.RegistrationException;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.security.AuthenticationServiceImpl;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.impl.CinemaHallServiceImpl;
import mate.academy.service.impl.MovieServiceImpl;
import mate.academy.service.impl.MovieSessionServiceImpl;
import mate.academy.service.impl.ShoppingCartServiceImpl;

public class Main {
    public static void main(String[] args) {
        MovieService movieService = new MovieServiceImpl();

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

        CinemaHallService cinemaHallService = new CinemaHallServiceImpl();
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

        MovieSessionService movieSessionService = new MovieSessionServiceImpl();
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService = new AuthenticationServiceImpl();
        User user;
        try {
            user = authenticationService.register("andriy@gmail.com", "1234");
        } catch (RegistrationException e) {
            throw new RuntimeException(e);
        }

        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl();

        shoppingCartService.registerNewShoppingCart(user);

        MovieSession movieSession = new MovieSession();
        movieSession.setShowTime(LocalDateTime.now());
        Movie movie = new Movie();
        movie.setDescription("HARRY POTTER DESC");
        movie.setTitle("Harry");
        movieSession.setMovie(movie);

        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setDescription("Hall desc");
        cinemaHall.setCapacity(123);
        movieSession.setCinemaHall(cinemaHall);

        movieService.add(movie);

        cinemaHallService.add(cinemaHall);

        movieSession.setMovie(movie);
        movieSession.setCinemaHall(cinemaHall);
        movieSessionService.add(movieSession);

        shoppingCartService.addSession(movieSession, user);

        ShoppingCart shoppingCart = shoppingCartService.getByUser(user);

        shoppingCartService.clear(shoppingCart);

    }
}
