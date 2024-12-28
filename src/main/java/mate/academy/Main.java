package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final Logger logger = Logger.getLogger(Main.class.getName());

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

        CinemaHallService cinemaHallService = (CinemaHallService) injector.getInstance(
                CinemaHallService.class);
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

        MovieSessionService movieSessionService = (MovieSessionService) injector.getInstance(
                MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        //Testing the functionality of ShoppingCartService and TicketDao.
        AuthenticationService authenticationService = (AuthenticationService) injector.getInstance(
                AuthenticationService.class);
        try {
            authenticationService.register("bob@gmail.com", "password");
            authenticationService.register("alice@gmail.com", "password");

            User loginBob = authenticationService.login("bob@gmail.com", "password");
            User loginAlice = authenticationService.login("alice@gmail.com", "password");
            List<User> users = List.of(loginBob, loginAlice);
            //1. Register new shopping carts.
            ShoppingCartService shoppingCartService = (ShoppingCartService) injector.getInstance(
                    ShoppingCartService.class);
            for (User user : users) {
                shoppingCartService.registerNewShoppingCart(user);
            }
            //2. Add new sessions and creating tickets for existing shopping carts.
            shoppingCartService.addSession(tomorrowMovieSession, loginBob);
            shoppingCartService.addSession(tomorrowMovieSession, loginAlice);
            //3. Retrieve shopping cart information by user.
            System.out.println(shoppingCartService.getByUser(loginBob));
            //4. Delete all tickets from an existing shopping cart.
            shoppingCartService.clear(shoppingCartService.getByUser(loginAlice));
            System.out.println(shoppingCartService.getByUser(loginAlice));
        } catch (RegistrationException e) {
            logger.log(Level.SEVERE, "Error during user registration: ", e);
            throw new RuntimeException("Failed to register a new user."
                    + " Please check your email or password.", e);
        } catch (AuthenticationException e) {
            logger.log(Level.SEVERE, "Error during user authentication: ", e);
            throw new RuntimeException("Failed to login in account, "
                    + "email or password is incorrect.");
        }
    }
}
