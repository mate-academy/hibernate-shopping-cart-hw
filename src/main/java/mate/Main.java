package mate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.dao.TicketDao;
import mate.exception.RegistrationException;
import mate.lib.Injector;
import mate.model.CinemaHall;
import mate.model.Movie;
import mate.model.MovieSession;
import mate.model.ShoppingCart;
import mate.model.Ticket;
import mate.model.User;
import mate.security.AuthenticationService;
import mate.service.CinemaHallService;
import mate.service.MovieService;
import mate.service.MovieSessionService;
import mate.service.ShoppingCartService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate");

    public static void main(String[] args) {
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);;
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        System.out.println("__________________________________");

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        CinemaHallService cinemaHallService = (CinemaHallService) injector
                .getInstance(CinemaHallService.class);;
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        System.out.println("__________________________________");

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSessionService movieSessionService = (MovieSessionService) injector
                .getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        System.out.println("__________________________________");

        // check validate ticket
        Ticket ticket = new Ticket();
        ticket.setMovieSession(yesterdayMovieSession);
        TicketDao ticketDao = (TicketDao) injector
                .getInstance(TicketDao.class);
        System.out.println(ticketDao.add(ticket));
        System.out.println("__________________________________");

        // register new user
        AuthenticationService authenticationService = (AuthenticationService)injector
                .getInstance(AuthenticationService.class);
        User bob = null;
        try {
            bob = authenticationService.register("bob@gmail.com", "12345678");
            System.out.println(bob);
        } catch (RegistrationException e) {
            throw new RuntimeException(e);
        }

        System.out.println("__________________________________");

        // check register() and getByUser() methods
        ShoppingCartService shoppingCartService = (ShoppingCartService)injector
                .getInstance(ShoppingCartService.class);

        shoppingCartService.registerNewShoppingCart(bob);
        ShoppingCart shoppingCart = shoppingCartService.getByUser(bob);
        System.out.println(shoppingCart);

        System.out.println("__________________________________");

        // check addSession method
        shoppingCartService.addSession(yesterdayMovieSession,bob);
        ShoppingCart shoppingCartByBob = shoppingCartService.getByUser(bob);
        System.out.println(shoppingCartByBob);

        // check clear method
        shoppingCartService.clear(shoppingCartByBob);
        System.out.println(shoppingCartByBob);
    }
}
