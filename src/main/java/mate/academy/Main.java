package mate.academy;

import java.time.LocalDateTime;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
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
        final UserService userService =
                (UserService) injector.getInstance(UserService.class);
        final ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        Movie movie = new Movie("Inception");
        movie.setDescription("Mind-bending thriller");
        movieService.add(movie);

        CinemaHall hall = new CinemaHall();
        hall.setCapacity(150);
        hall.setDescription("Main Hall");
        cinemaHallService.add(hall);

        MovieSession session = new MovieSession();
        session.setMovie(movie);
        session.setCinemaHall(hall);
        session.setShowTime(LocalDateTime.now().plusDays(1));
        movieSessionService.add(session);

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        userService.add(user);

        shoppingCartService.registerNewShoppingCart(user);
        shoppingCartService.addSession(session, user);

        ShoppingCart cart = shoppingCartService.getByUser(user);
        System.out.println("Shopping cart for user: " + user.getEmail());
        cart.getTickets().forEach(ticket -> {
            System.out.println("Ticket: movie="
                    + ticket.getMovieSession().getMovie().getTitle()
                    + ", time="
                    + ticket.getMovieSession().getShowTime());
        });

        shoppingCartService.clear(cart);
        System.out.println("Shopping cart cleared.");
        System.out.println("Tickets in cart after clearing: "
                + shoppingCartService.getByUser(user).getTickets().size());
    }
}
