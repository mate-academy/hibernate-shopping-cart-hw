package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        MovieService movieService = (MovieService) injector
                .getInstance(MovieService.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing.");
        movieService.add(fastAndFurious);
        System.out.println("Added movie: " + movieService.get(fastAndFurious.getId()));

        CinemaHallService cinemaHallService = (CinemaHallService) injector
                .getInstance(CinemaHallService.class);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");
        cinemaHallService.add(firstCinemaHall);

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");
        cinemaHallService.add(secondCinemaHall);

        System.out.println("All cinema halls: " + cinemaHallService.getAll());

        final MovieSessionService movieSessionService = (MovieSessionService) injector
                .getInstance(MovieSessionService.class);

        MovieSession tomorrowSession = new MovieSession();
        tomorrowSession.setCinemaHall(firstCinemaHall);
        tomorrowSession.setMovie(fastAndFurious);
        tomorrowSession.setShowTime(LocalDateTime.now().plusDays(1L));
        movieSessionService.add(tomorrowSession);

        MovieSession todaySession = new MovieSession();
        todaySession.setCinemaHall(secondCinemaHall);
        todaySession.setMovie(fastAndFurious);
        todaySession.setShowTime(LocalDateTime.now());
        movieSessionService.add(todaySession);

        System.out.println("Available sessions: "
                + movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authService = (AuthenticationService) injector
                .getInstance(AuthenticationService.class);

        User user = registerUser(authService, "alice@example.com", "password123");

        ShoppingCartService shoppingCartService = (ShoppingCartService) injector
                .getInstance(ShoppingCartService.class);

        System.out.println("\n=== Testing Shopping Cart ===");

        ShoppingCart emptyCart = shoppingCartService.getByUser(user);
        System.out.println("Empty cart tickets: " + emptyCart.getTickets().size());

        System.out.println("\n--- Adding sessions to cart ---");
        shoppingCartService.addSession(tomorrowSession, user);
        shoppingCartService.addSession(todaySession, user);

        ShoppingCart cartWithTickets = shoppingCartService.getByUser(user);
        System.out.println("Cart with tickets: " + cartWithTickets.getTickets().size());
        cartWithTickets.getTickets().forEach(ticket ->
                System.out.println("  - Ticket for: "
                        + ticket.getMovieSession().getMovie().getTitle()));

        System.out.println("\n--- Clearing cart ---");
        shoppingCartService.clear(cartWithTickets);

        ShoppingCart clearedCart = shoppingCartService.getByUser(user);
        System.out.println("Cleared cart tickets: " + clearedCart.getTickets().size());

        System.out.println("\n--- Testing with another user ---");
        User bob = registerUser(authService, "bob@example.com", "pass456");

        shoppingCartService.addSession(tomorrowSession, bob);
        ShoppingCart bobCart = shoppingCartService.getByUser(bob);
        System.out.println("Bob's cart tickets: " + bobCart.getTickets().size());

        System.out.println("\n=== All tests completed successfully! ===");
    }

    private static User registerUser(AuthenticationService authService,
                                     String email,
                                     String password) {
        try {
            User user = authService.register(email, password);
            System.out.println("Registered user: " + user);
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Registration failed for " + email, e);
        }
    }
}
