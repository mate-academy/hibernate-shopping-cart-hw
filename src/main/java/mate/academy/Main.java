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
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        // Create some test data
        Movie fastAndFurious = new Movie();
        fastAndFurious.setTitle("Fast and Furious");
        movieService.add(fastAndFurious);

        Movie multiplex = new Movie();
        multiplex.setTitle("Multiplex");
        movieService.add(multiplex);

        CinemaHall redHall = new CinemaHall();
        redHall.setCapacity(100);
        redHall.setDescription("red");
        CinemaHallService cinemaHallService = (CinemaHallService) injector
                .getInstance(CinemaHallService.class);
        cinemaHallService.add(redHall);

        MovieSession morningSession = new MovieSession();
        morningSession.setMovie(fastAndFurious);
        morningSession.setCinemaHall(redHall);
        morningSession.setShowTime(LocalDateTime.now().plusHours(2));
        MovieSessionService movieSessionService = (MovieSessionService) injector
                .getInstance(MovieSessionService.class);
        movieSessionService.add(morningSession);

        UserService userService = (UserService) injector.getInstance(UserService.class);
        User bob = new User();
        bob.setEmail("bob@example.com");
        bob.setPassword("123");
        userService.add(bob);

        User alice = new User();
        alice.setEmail("alice@example.com");
        alice.setPassword("456");
        userService.add(alice);

        System.out.println("\n--- Testing ShoppingCartService ---");

        ShoppingCartService shoppingCartService = (ShoppingCartService) injector
                .getInstance(ShoppingCartService.class);
        shoppingCartService.registerNewShoppingCart(bob);
        ShoppingCart bobShoppingCart = shoppingCartService.getByUser(bob);
        System.out.println("Bob's Shopping Cart after registration: " + bobShoppingCart);

        shoppingCartService.registerNewShoppingCart(alice);
        ShoppingCart aliceShoppingCart = shoppingCartService.getByUser(alice);
        System.out.println("Alice's Shopping Cart after registration: " + aliceShoppingCart);

        // Test addSession
        shoppingCartService.addSession(morningSession, bob);
        bobShoppingCart = shoppingCartService.getByUser(bob);
        System.out.println("Bob's Shopping Cart after adding a session: " + bobShoppingCart);

        MovieSession eveningSession = new MovieSession();
        eveningSession.setMovie(fastAndFurious);
        eveningSession.setCinemaHall(redHall);
        eveningSession.setShowTime(LocalDateTime.now().plusHours(5));
        movieSessionService.add(eveningSession);
        shoppingCartService.addSession(eveningSession, bob);
        bobShoppingCart = shoppingCartService.getByUser(bob);
        System.out.println("Bob's Shopping Cart after adding another session: " + bobShoppingCart);

        // Test getByUser
        ShoppingCart retrievedBobCart = shoppingCartService.getByUser(bob);
        System.out.println("Retrieved Bob's Shopping Cart: " + retrievedBobCart);
        ShoppingCart retrievedAliceCart = shoppingCartService.getByUser(alice);
        System.out.println("Retrieved Alice's Shopping Cart: " + retrievedAliceCart);

        // Test clear
        shoppingCartService.clear(retrievedBobCart);
        System.out.println("Bob's Shopping Cart after clearing: "
                + shoppingCartService.getByUser(bob));
        shoppingCartService.clear(retrievedAliceCart);
        System.out.println("Alice's Shopping Cart after clearing: "
                + shoppingCartService.getByUser(alice));
    }
}
