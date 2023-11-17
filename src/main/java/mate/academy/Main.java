package mate.academy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.dao.impl.ShoppingCartDaoImpl;
import mate.academy.dao.impl.TicketDaoImpl;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
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
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final String USER_EMAIL = "user123@gmail";
    private static final String USER_PASSWORD = "user123";

    public static void main(String[] args) throws RegistrationException, AuthenticationException {
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

        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        //System.out.println(movieSessionService.findAvailableSessions(
        //        fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService
                = (AuthenticationService) injector.getInstance(AuthenticationService.class);
        authenticationService.register(USER_EMAIL, USER_PASSWORD);
        authenticationService.login(USER_EMAIL, USER_PASSWORD);

        UserService userService = (UserService) injector.getInstance(UserService.class);
        Optional<User> optionalUser = userService.findByEmail(USER_EMAIL);

        optionalUser.ifPresentOrElse(a -> System.out.println(optionalUser.get()),
                () -> System.out.println("User is null."));
        User user = optionalUser.get();

        TicketDao ticketDao = new TicketDaoImpl();
        Ticket ticket = new Ticket();
        ticket.setMovieSession(tomorrowMovieSession);
        ticket.setUser(user);
        ticketDao.add(ticket);

        ShoppingCartDao shoppingCartDao = new ShoppingCartDaoImpl();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setTickets(List.of(ticket));
        ShoppingCart shoppingCartFromDB = shoppingCartDao.add(shoppingCart);
        System.out.println(shoppingCartFromDB);

        Ticket ticket01 = new Ticket();
        ticket01.setMovieSession(yesterdayMovieSession);
        ticket01.setUser(user);
        ticketDao.add(ticket01);

        shoppingCartFromDB.setTickets(List.of(ticket, ticket01));
        shoppingCartDao.update(shoppingCartFromDB);
        System.out.println(shoppingCartFromDB);

    }
}
