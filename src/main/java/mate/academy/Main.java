package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.RegistrationException;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.impl.CinemaHallServiceImpl;
import mate.academy.service.impl.MovieServiceImpl;
import mate.academy.service.impl.MovieSessionServiceImpl;
import mate.academy.service.impl.ShoppingCartServiceImpl;

public class Main {
    public static void main(String[] args) throws RegistrationException {
        User bob = new User();
        bob.setEmail("bob@gmail.com");
        bob.setPassword("1234");

        MovieService filmService = new MovieServiceImpl();
        Movie Ford_vs_Ferrari = new Movie("Ford v. Ferrari");
        Ford_vs_Ferrari.setDescription("The film tells the true story of the "
            + "struggle between the Ford and Ferrari teams that erupted at "
            + "the 1966 Le Mans race.");
        filmService.add(Ford_vs_Ferrari);

        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(300);
        cinemaHall.setDescription("Main Hall");


        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(Ford_vs_Ferrari);
        movieSession.setCinemaHall(cinemaHall);
        movieSession.setShowTime(LocalDateTime.now().plusDays(1));

        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl();
        shoppingCartService.registerNewShoppingCart(bob);
        shoppingCartService.addSession(movieSession, bob);
        shoppingCartService.getByUser(bob);

        ShoppingCart bobCart = shoppingCartService.getByUser(bob);
        shoppingCartService.clear(bobCart);

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
    }
}
