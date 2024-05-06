package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
import mate.academy.service.impl.CinemaHallServiceImpl;
import mate.academy.service.impl.MovieServiceImpl;
import mate.academy.service.impl.MovieSessionServiceImpl;
import mate.academy.service.impl.ShoppingCartServiceImpl;
import mate.academy.service.impl.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        MovieService movieService = new MovieServiceImpl();

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

        CinemaHallService cinemaHallService = new CinemaHallServiceImpl();
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

        MovieSessionService movieSessionService = new MovieSessionServiceImpl();
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        User bobUser = new User();
        bobUser.setEmail("bobUser@gmail.com");
        bobUser.setPassword("potato787");

        Ticket yesterdaySessionTicket = new Ticket();
        yesterdaySessionTicket.setUser(bobUser);
        yesterdaySessionTicket.setMovieSession(yesterdayMovieSession);
        Ticket tomorrowSessionTicket = new Ticket();
        tomorrowSessionTicket.setUser(bobUser);
        tomorrowSessionTicket.setMovieSession(tomorrowMovieSession);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(bobUser);
        shoppingCart.setTickets(List.of(yesterdaySessionTicket, tomorrowSessionTicket));
        bobUser.setShoppingCart(shoppingCart);
        UserService userService = new UserServiceImpl();
        userService.add(bobUser);
        System.out.println(userService.findByEmail(bobUser.getEmail()));

        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl();
        shoppingCartService.registerNewShoppingCart(bobUser);

        shoppingCartService.addSession(yesterdayMovieSession, bobUser);
        shoppingCartService.addSession(tomorrowMovieSession, bobUser);
        System.out.println(shoppingCartService.getByUser(bobUser));
        shoppingCartService.clear(shoppingCart);
    }
}
