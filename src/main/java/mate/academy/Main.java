package mate.academy;

import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
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

    public static void main(String[] args) throws RegistrationException, AuthenticationException {
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("Nice children film");

        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        movieService.add(fastAndFurious);

        CinemaHall blueHall = new CinemaHall();
        blueHall.setDescription("Cute blue hall");
        blueHall.setCapacity(60);

        CinemaHallService cinemaHallService
                = (CinemaHallService) injector.getInstance(CinemaHallService.class);
        cinemaHallService.add(blueHall);

        MovieSession todaySession = new MovieSession();
        todaySession.setMovie(fastAndFurious);
        todaySession.setCinemaHall(blueHall);
        todaySession.setShowTime(LocalDateTime.now());

        MovieSessionService movieSessionService
                = (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(todaySession);

        User bob = new User();
        bob.setEmail("bob@gmail.com");
        bob.setPassword("qwerty");

        User alice = new User();
        alice.setEmail("alice@gmail.com");
        alice.setPassword("qwerty");

        AuthenticationService authenticationService
                = (AuthenticationService) injector.getInstance(AuthenticationService.class);
        authenticationService.register(bob.getEmail(), bob.getPassword());
        authenticationService.register(alice.getEmail(), alice.getPassword());

        User loginBob = authenticationService.login(bob.getEmail(), bob.getPassword());
        User loginAlice = authenticationService.login(alice.getEmail(), alice.getPassword());

        ShoppingCartService shoppingCartService
                = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        System.out.println(shoppingCartService.getByUser(loginBob));
        System.out.println(shoppingCartService.getByUser(loginAlice));

        shoppingCartService.addSession(todaySession, loginAlice);
        shoppingCartService.addSession(todaySession, loginAlice);
        ShoppingCart aliceShoppingCart = shoppingCartService.getByUser(loginAlice);
        System.out.println(aliceShoppingCart);

        shoppingCartService.addSession(todaySession, loginBob);
        shoppingCartService.addSession(todaySession, loginBob);
        ShoppingCart bobShoppingCart = shoppingCartService.getByUser(loginBob);
        System.out.println(bobShoppingCart);

        shoppingCartService.clear(bobShoppingCart);
        System.out.println(bobShoppingCart);
        shoppingCartService.clear(aliceShoppingCart);
        System.out.println(aliceShoppingCart);
    }
}
