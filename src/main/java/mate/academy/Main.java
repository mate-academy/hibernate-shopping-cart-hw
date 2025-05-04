package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.dao.UserDao;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.User;
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

        CinemaHallService cinemaHallService = (CinemaHallService) injector.getInstance(
                            CinemaHallService.class);
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

        MovieSessionService movieSessionService = (MovieSessionService) injector.getInstance(
                                    MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        User user = new User();
        user.setEmail("email@gmail.com");
        user.setPassword("1234");
        UserDao userDao = (UserDao) injector.getInstance(UserDao.class);
        System.out.println("Added user with email: email@gmail.com to DB. "
                            + userDao.findByEmail("email@gmail.com"));
        ShoppingCartService shoppingCartService = (ShoppingCartService) injector.getInstance(
                                    ShoppingCartService.class);
        shoppingCartService.registerNewShoppingCart(user);
        System.out.println("Shopping cart of user: "
                + user + " is: " + shoppingCartService.getByUser(user));

        MovieSession todayMovieSession = new MovieSession();
        todayMovieSession.setMovie(fastAndFurious);
        todayMovieSession.setCinemaHall(firstCinemaHall);
        todayMovieSession.setShowTime(LocalDateTime.now());
        movieSessionService.add(todayMovieSession);

        shoppingCartService.addSession(todayMovieSession, user);
        System.out.println("Ticket for fastAndFurious, firstCinemaHall, today date was added: "
                + shoppingCartService.getByUser(user).getTickets());

        shoppingCartService.clear(shoppingCartService.getByUser(user));
        System.out.println("Cleared shopping cart. It should be empty: "
                + shoppingCartService.getByUser(user).getTickets());
    }
}
