package mate.academy;

import java.time.LocalDateTime;
import mate.academy.dao.TicketDao;
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
import mate.academy.service.ShoppingCartService;

public class Main {
    public static void main(String[] args) throws AuthenticationException {
        Injector injector = Injector.getInstance("mate.academy");
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
        //System.out.println(movieSessionService.findAvailableSessions(
        //fastAndFurious.getId(), LocalDate.now()));
        AuthenticationService auth =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        try {
            auth.register("globaroman@gmail.com", "qwerty");
            auth.register("test@gmail.com", "test123");
        } catch (RegistrationException e) {
            throw new RuntimeException(e);
        }
        User user1 = auth.login("globaroman@gmail.com", "qwerty");
        User user2 = auth.login("test@gmail.com", "test123");

        ShoppingCartService shopCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        shopCartService.addSession(tomorrowMovieSession, user1);
        shopCartService.addSession(tomorrowMovieSession, user2);
        ShoppingCart cartFromDB1 = shopCartService.getByUser(user1);
        System.out.println();
        System.out.println(cartFromDB1);
        shopCartService.clear(cartFromDB1);
        System.out.println(cartFromDB1);
        ShoppingCart cartFromDB2 = shopCartService.getByUser(user2);
        System.out.println(cartFromDB2);
        shopCartService.clear(cartFromDB2);
        System.out.println(cartFromDB2);
        System.out.println();

        TicketDao ticketDao = new TicketDaoImpl();
        Ticket ticket1 = new Ticket(yesterdayMovieSession, user1);
        Ticket ticket2 = new Ticket(tomorrowMovieSession, user2);
        ticketDao.add(ticket1);
        ticketDao.add(ticket2);
        System.out.println(ticket1);
        System.out.println(ticket2);

    }
}
