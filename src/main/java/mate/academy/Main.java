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

        CinemaHallService cinemaHallService
                = (CinemaHallService) injector.getInstance(CinemaHallService.class);
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

        MovieSessionService movieSessionService
                = (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService
                = (AuthenticationService) injector.getInstance(AuthenticationService.class);
        User adam = null;
        User eva = null;
        try {
            adam = authenticationService.register("adam@gmail.com", "adam");
            eva = authenticationService.register("eva@gamil.com", "eva");
        } catch (RegistrationException e) {
            throw new RuntimeException("Users was not registered");
        }
        System.out.println("Registration");
        System.out.println("---------------------------------------------------------");
        try {
            authenticationService.login(adam.getEmail(), "adam");
            authenticationService.login(eva.getEmail(), "eva");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Can't login users");
        }
        System.out.println("Authentication");
        System.out.println("---------------------------------------------------------");
        ShoppingCartService shoppingCartService
                = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        ShoppingCart adamCart = shoppingCartService.getByUser(adam);
        ShoppingCart evaCart = shoppingCartService.getByUser(eva);
        System.out.println(adamCart);
        System.out.println(evaCart);
        System.out.println("Get shopping cart by user");
        System.out.println("---------------------------------------------------------");
        shoppingCartService.addSession(yesterdayMovieSession, adam);
        shoppingCartService.addSession(tomorrowMovieSession, eva);
        System.out.println(shoppingCartService.getByUser(adam));
        System.out.println(shoppingCartService.getByUser(eva));
        System.out.println("Add new session to shopping cart");
        System.out.println("---------------------------------------------------------");
        shoppingCartService.clear(adamCart);
        shoppingCartService.clear(evaCart);
        System.out.println(shoppingCartService.getByUser(adam));
        System.out.println(shoppingCartService.getByUser(eva));
        System.out.println("Delete all ticket from shopping cart");
        System.out.println("---------------------------------------------------------");
    }
}
