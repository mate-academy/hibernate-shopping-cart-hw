package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final MovieService movieService
            = (MovieService) injector.getInstance(MovieService.class);
    private static final CinemaHallService cinemaHallService
            = (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static final MovieSessionService movieSessionService
            = (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static final AuthenticationService authenticationService
            = (AuthenticationService) injector.getInstance(AuthenticationService.class);
    private static final ShoppingCartService shoppingCartService
            = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
    private static final UserService userService
            = (UserService) injector.getInstance(UserService.class);
    private static final TicketDao ticketDao = (TicketDao) injector.getInstance(TicketDao.class);

    public static void main(String[] args) {
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

        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        User userIra = new User();
        userIra.setEmail("123@gmail.com");
        userIra.setPassword("qwerty");
        userService.add(userIra);

        Ticket ticket1 = new Ticket();
        ticket1.setMovieSession(tomorrowMovieSession);
        ticket1.setUser(userIra);
        ticketDao.add(ticket1);

        Ticket ticket2 = new Ticket();
        ticket2.setMovieSession(yesterdayMovieSession);
        ticket2.setUser(userIra);
        ticketDao.add(ticket2);

        Ticket ticket3 = new Ticket();
        ticket3.setMovieSession(tomorrowMovieSession);
        ticket3.setUser(userIra);
        ticketDao.add(ticket3);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(userIra);
        shoppingCart.setTickets(List.of(ticket1, ticket2, ticket3));

        shoppingCartService.registerNewShoppingCart(shoppingCart.getUser());
        shoppingCartService.addSession(tomorrowMovieSession,userService
                .findByEmail(userIra.getEmail()).orElseThrow());
        System.out.println(shoppingCartService.getByUser(userIra));
        shoppingCartService.clear(shoppingCartService.getByUser(userIra));
        System.out.println(shoppingCartService.getByUser(userIra));
    }
}
