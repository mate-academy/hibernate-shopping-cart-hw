package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.TicketDao;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.security.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        final MovieService movieService =
                (MovieService) injector.getInstance(MovieService.class);
        final CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        final MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        final ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        final AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        final UserService userService =
                (UserService) injector.getInstance(UserService.class);
        final TicketDao ticketDao = (TicketDao) injector.getInstance(TicketDao.class);

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

        try {
            authenticationService.register("sofia@gmail.com", "lu4shii_mentor_na_planete_<3");
        } catch (RegistrationException e) {
            throw new RuntimeException("Can't register");
        }

        Ticket ticket = new Ticket();
        ticket.setMovieSession(tomorrowMovieSession);
        ticket.setUser(userService.findByEmail("sofia@gmail.com").orElseThrow());
        ticketDao.add(ticket);

        Ticket ticket2 = new Ticket();
        ticket2.setMovieSession(yesterdayMovieSession);
        ticket2.setUser(userService.findByEmail("sofia@gmail.com").orElseThrow());
        ticketDao.add(ticket2);

        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);
        tickets.add(ticket2);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(userService.findByEmail("sofia@gmail.com").orElseThrow());
        shoppingCart.setTickets(tickets);
        System.out.println(shoppingCartService
                .getByUser(userService.findByEmail("sofia@gmail.com").orElseThrow()));

    }
}
