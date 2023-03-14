package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
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
import mate.academy.util.HashUtil;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    private static MovieSessionService movieSessionService =
            (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static ShoppingCartService shoppingCartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

    private static MovieService movieService =
            (MovieService) injector.getInstance(MovieService.class);

    private static CinemaHallService cinemaHallService =
            (CinemaHallService) injector.getInstance(CinemaHallService.class);

    private static UserService userService =
            (UserService) injector.getInstance(UserService.class);

    private static TicketDao ticketDao =
            (TicketDao) injector.getInstance(TicketDao.class);
    private static ShoppingCartDao shoppingCartDao =
            (ShoppingCartDao) injector.getInstance(ShoppingCartDao.class);

    public static void main(String[] args) throws RegistrationException, AuthenticationException {

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

        User ivo = new User();
        ivo.setSalt(HashUtil.getSalt());
        ivo.setEmail("ivoBobul123@gmail.com");
        ivo.setPassword("IvoBobul");

        User sendulesa = new User();
        sendulesa.setSalt(HashUtil.getSalt());
        sendulesa.setEmail("senudelesa@gmail.com");
        sendulesa.setPassword("Sendulesa");

        System.out.println(userService.add(ivo));
        System.out.println(userService.add(sendulesa));

        Ticket ivoTicket = new Ticket();
        ivoTicket.setUser(ivo);
        ivoTicket.setMovieSession(tomorrowMovieSession);

        Ticket sendulesaTicket = new Ticket();
        sendulesaTicket.setMovieSession(tomorrowMovieSession);
        sendulesaTicket.setUser(sendulesa);

        System.out.println(ticketDao.add(ivoTicket));
        System.out.println(ticketDao.add(sendulesaTicket));

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setTickets(List.of(ivoTicket));
        shoppingCart.setUser(ivo);

        shoppingCartDao.add(shoppingCart);

        shoppingCart.setUser(ivo);
        shoppingCart.setTickets(Collections.emptyList());
        shoppingCartDao.update(shoppingCart);

        shoppingCartService.addSession(yesterdayMovieSession, ivo);

        System.out.println(shoppingCartService.getByUser(ivo));

        User childIvo = new User();
        childIvo.setPassword("ChildIvo");
        childIvo.setEmail("childIvo@gmail.com");
        userService.add(childIvo);

        shoppingCartService.registerNewShoppingCart(childIvo);

        shoppingCartService.clear(shoppingCart);
    }
}
