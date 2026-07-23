package mate.academy;

import mate.academy.dao.TicketDao;
import mate.academy.dao.impl.TicketDaoImpl;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.User;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {

    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        final UserService userService = (UserService) injector.getInstance(UserService.class);
        final ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        final MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        final MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        final CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);

        final TicketDao ticketDao = new TicketDaoImpl();

        User bob = new User();
        bob.setEmail("bob@bob.com");
        bob.setPassword("bob");
        userService.add(bob);

        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setDescription("Cinema City");
        cinemaHall.setCapacity(100);
        cinemaHallService.add(cinemaHall);

        Movie movie = new Movie();
        movie.setTitle("Detective Monk");
        movie.setDescription("Movie about genius detective");
        movieService.add(movie);

        shoppingCartService.registerNewShoppingCart(bob);

        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(movie);
        movieSession.setCinemaHall(cinemaHall);
        movieSessionService.add(movieSession);

        shoppingCartService.addSession(movieSession, bob);
        shoppingCartService.getByUser(bob);
        shoppingCartService.clear(shoppingCartService.getByUser(bob));
    }
}
