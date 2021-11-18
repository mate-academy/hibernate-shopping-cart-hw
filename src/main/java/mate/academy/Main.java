package mate.academy;

import java.time.LocalDateTime;
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

        final MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        final CinemaHallService cinemaHallService = (CinemaHallService) injector
                .getInstance(CinemaHallService.class);
        final MovieSessionService movieSessionService = (MovieSessionService) injector
                .getInstance(MovieSessionService.class);
        final UserService userService = (UserService) injector.getInstance(UserService.class);
        final ShoppingCartService shoppingCartService = (ShoppingCartService) injector
                .getInstance(ShoppingCartService.class);
        final TicketDao ticketDao = (TicketDao) injector.getInstance(TicketDao.class);
        final ShoppingCartDao shoppingCartDao = (ShoppingCartDao) injector
                .getInstance(ShoppingCartDao.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");
        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

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

        User firstUser = new User();
        firstUser.setEmail("firstuser@gmail.com");
        firstUser.setPassword("qwerty");
        User secondUser = new User();
        secondUser.setEmail("seconduser@gmail.com");
        secondUser.setPassword("1234");
        userService.add(firstUser);
        userService.add(secondUser);
        User thirdUser = new User();
        thirdUser.setEmail("thirduser@gmail.com");
        thirdUser.setPassword("666");

        Ticket firstTicket = new Ticket();
        firstTicket.setMovieSession(tomorrowMovieSession);
        firstTicket.setUser(firstUser);
        Ticket secondTicket = new Ticket();
        secondTicket.setMovieSession(yesterdayMovieSession);
        secondTicket.setUser(secondUser);
        ticketDao.add(firstTicket);
        ticketDao.add(secondTicket);

        ShoppingCart firstShoppingCart = new ShoppingCart();
        firstShoppingCart.setTickets(List.of(firstTicket));
        firstShoppingCart.setUser(firstUser);
        ShoppingCart secondShoppingCart = new ShoppingCart();
        secondShoppingCart.setUser(secondUser);
        secondShoppingCart.setTickets(List.of(secondTicket));

        // test all created methods
        shoppingCartDao.add(secondShoppingCart);
        shoppingCartDao.add(firstShoppingCart);
        System.out.println(shoppingCartService.getByUser(firstUser));
        shoppingCartDao.update(firstShoppingCart);
        shoppingCartService.addSession(tomorrowMovieSession,secondUser);
        shoppingCartService.registerNewShoppingCart(thirdUser);
        shoppingCartService.clear(firstShoppingCart);
    }
}
