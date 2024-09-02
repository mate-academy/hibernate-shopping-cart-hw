package mate.academy;

import java.util.List;
import mate.academy.dao.TicketDao;
import mate.academy.dao.impl.TicketDaoImpl;
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
        CinemaHall cinemaHall = new CinemaHall(100, "Cinema Hall #1");
        CinemaHall cinemaHall1 = new CinemaHall(250, "Cinema Hall #2");
        CinemaHall cinemaHall2 = new CinemaHall(300, "Cinema Hall #3");
        CinemaHallService cinemaHallService = (CinemaHallService) injector
                .getInstance(CinemaHallService.class);
        cinemaHallService.add(cinemaHall);
        cinemaHallService.add(cinemaHall1);
        cinemaHallService.add(cinemaHall2);
        Movie movie = new Movie("Scary movie", "Scary description");
        Movie movie1 = new Movie("Fast and furious", "Fast description");
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        movieService.add(movie);
        movieService.add(movie1);
        MovieSession movieSession = new MovieSession(movie, cinemaHall);
        MovieSessionService movieSessionService = (MovieSessionService) injector
                .getInstance(MovieSessionService.class);
        movieSessionService.add(movieSession);
        User user = new User("mykhailo123", "qwerty");
        UserService userService = (UserService) injector.getInstance(UserService.class);
        userService.add(user);
        ShoppingCartService shoppingCartService = (ShoppingCartService) injector
                .getInstance(ShoppingCartService.class);
        shoppingCartService.addSession(movieSession, user);
        MovieSession movieSession1 = new MovieSession(movie1, cinemaHall1);
        MovieSession movieSession2 = new MovieSession(movie1, cinemaHall2);
        movieSessionService.add(movieSession1);
        movieSessionService.add(movieSession2);
        Ticket ticket = new Ticket(movieSession1, user);
        Ticket ticket2 = new Ticket(movieSession2, user);
        TicketDao ticketDao = new TicketDaoImpl();
        ticketDao.add(ticket);
        ticketDao.add(ticket2);
        List<Ticket> tickets = List.of(ticket, ticket2);
        ShoppingCart shoppingCart = new ShoppingCart(user, tickets);
        shoppingCartService.addSession(movieSession1, user);
        shoppingCartService.addSession(movieSession2, user);
        shoppingCartService.registerNewShoppingCart(shoppingCart);
    }
}
