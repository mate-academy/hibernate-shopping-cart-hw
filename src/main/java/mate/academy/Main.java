package mate.academy;

import java.time.LocalDateTime;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static CinemaHallService cinemaHallService;
    private static MovieService movieService;
    private static AuthenticationService authService;
    private static MovieSessionService movieSessionService;
    private static TicketDao ticketDao;
    private static ShoppingCartService shoppingCartService;

    private static final int EMAIL_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;
    private static final String[] USER_1 = {"u1@gmail.com", "qwerty" };
    private static final String[] USER_2 = {"u2@gmail.com", "qwerty" };
    private static final String[] USER_3 = {"u3@gmail.com", "asdfg" };
    private static User user1;
    private static User user2;
    private static User user3;

    public static void main(String[] args) {
        cinemaHallService = (CinemaHallService) injector.getInstance(CinemaHallService.class);
        movieService = (MovieService) injector.getInstance(MovieService.class);
        authService = (AuthenticationService) injector
                .getInstance(AuthenticationService.class);
        movieSessionService = (MovieSessionService) injector.getInstance(MovieSessionService.class);
        ticketDao = (TicketDao) injector.getInstance(TicketDao.class);
        shoppingCartService = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        initCinemaHalls();
        initMovies();
        initUsers();
        initMovieSessions();

        shoppingCartService.addSession(movieSessionService.get(1L),user2);
        shoppingCartService.addSession(movieSessionService.get(1L),user2);
        shoppingCartService.addSession(movieSessionService.get(2L),user1);
        shoppingCartService.addSession(movieSessionService.get(1L),user1);

        System.out.println("SHOPPING CART FOR " + user1);
        System.out.println(shoppingCartService.getByUser(user1));
        shoppingCartService.clear(shoppingCartService.getByUser(user1));
        System.out.println("CLEARED SHOPPING CART FOR " + user1);
        System.out.println("GET BY USER: " + shoppingCartService.getByUser(user1));
    }

    private static void initMovies() {
        movieService.add(new Movie("Fast and Furious",
                "An action film about street racing, heists, and spies."));
        movieService.add(new Movie("Brave heart", "Movie about brave hero."));
        movieService.add(new Movie("Home alone",
                "Movie about a child who was left alone at home "));
    }

    private static void initCinemaHalls() {
        cinemaHallService.add(new CinemaHall(100, "RED hall"));
        cinemaHallService.add(new CinemaHall(50, "GREEN hall"));
        cinemaHallService.add(new CinemaHall(500, "BIG hall"));
    }

    private static void initMovieSessions() {
        Movie movie;
        CinemaHall cinemaHall;
        LocalDateTime showTime;
        movie = movieService.get(1L);
        cinemaHall = cinemaHallService.get(1L);
        showTime = LocalDateTime.of(2022, 8, 02, 10, 00);
        movieSessionService.add(new MovieSession(movie, cinemaHall, showTime));
        movie = movieService.get(2L);
        cinemaHall = cinemaHallService.get(2L);
        showTime = LocalDateTime.of(2022, 8, 02, 12, 00);
        movieSessionService.add(new MovieSession(movie, cinemaHall, showTime));
        movie = movieService.get(2L);
        cinemaHall = cinemaHallService.get(3L);
        showTime = LocalDateTime.of(2022, 8, 02, 14, 00);
        movieSessionService.add(new MovieSession(movie, cinemaHall, showTime));
        movie = movieService.get(2L);
        cinemaHall = cinemaHallService.get(2L);
        showTime = LocalDateTime.of(2022, 8, 02, 18, 00);
        movieSessionService.add(new MovieSession(movie, cinemaHall, showTime));
    }

    private static void initUsers() {
        try {
            authService.register(USER_1[EMAIL_INDEX], USER_1[PASSWORD_INDEX]);
            user1 = authService.login(USER_1[EMAIL_INDEX], USER_1[PASSWORD_INDEX]);
            authService.register(USER_2[EMAIL_INDEX], USER_2[PASSWORD_INDEX]);
            authService.register(USER_3[EMAIL_INDEX], USER_3[PASSWORD_INDEX]);
        } catch (Exception e) {
            System.out.println("Can't register user " + e);
        }
        try {
            user1 = authService.login(USER_1[EMAIL_INDEX], USER_1[PASSWORD_INDEX]);
            user2 = authService.login(USER_2[EMAIL_INDEX], USER_2[PASSWORD_INDEX]);
            user3 = authService.login(USER_3[EMAIL_INDEX], USER_3[PASSWORD_INDEX]);
        } catch (Exception e) {
            System.out.println("Can't login user " + e);
        }

    }
}
