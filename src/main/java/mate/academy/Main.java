package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

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

        CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
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
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        User userBob = new User();
        userBob.setEmail("bob@gmail.com");
        userBob.setPassword("bobpassword");

        User userAlice = new User();
        userAlice.setEmail("alice@gmail.com");
        userAlice.setPassword("alicepassword");

        UserService userService = (UserService) injector.getInstance(UserService.class);
        System.out.println(userService.add(userBob));
        System.out.println(userService.add(userAlice));

        System.out.println(userService.findByEmail("bob@gmail.com"));
        System.out.println(userService.findByEmail("alice@gmail.com"));

        Ticket ticketBob = new Ticket();
        ticketBob.setUser(userBob);
        ticketBob.setMovieSession(yesterdayMovieSession);

        Ticket ticketAlice = new Ticket();
        ticketAlice.setUser(userBob);
        ticketAlice.setMovieSession(yesterdayMovieSession);

        TicketDao ticketDao = (TicketDao) injector.getInstance(TicketDao.class);
        System.out.println(ticketDao.add(ticketBob));
        System.out.println(ticketDao.add(ticketAlice));

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setTickets(List.of(ticketBob));
        shoppingCart.setUser(userBob);

        ShoppingCartDao shoppingCartDao =
                (ShoppingCartDao) injector.getInstance(ShoppingCartDao.class);
        shoppingCartDao.add(shoppingCart);

        shoppingCart.setUser(userBob);
        shoppingCart.setTickets(Collections.emptyList());
        shoppingCartDao.update(shoppingCart);

        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        shoppingCartService.addSession(yesterdayMovieSession, userBob);

        System.out.println(shoppingCartService.getByUser(userBob));

        User userJohn = new User();
        userJohn.setEmail("john@gmail.com");
        userJohn.setPassword("johnpassword");
        userService.add(userJohn);

        shoppingCartService.registerNewShoppingCart(userJohn);

        shoppingCartService.clear(shoppingCart);
    }
}
