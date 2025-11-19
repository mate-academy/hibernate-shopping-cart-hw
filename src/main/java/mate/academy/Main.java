package mate.academy;

import java.time.LocalDateTime;
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

    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");

        final MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        final CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        final MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        final AuthenticationService authService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        final ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        // 1️⃣ Rejestracja użytkownika
        User user;
        try {
            user = authService.register("test@gmail.com", "1111");
        } catch (RegistrationException e) {
            System.out.println("Nie udało się zarejestrować użytkownika: " + e.getMessage());
            return;
        }

        // 2️⃣ Rejestracja pustego koszyka
        shoppingCartService.registerNewShoppingCart(user);

        // 3️⃣ Dodanie filmu
        final Movie movie = new Movie("Matrix");
        movie.setDescription("Sci-fi classic");
        movieService.add(movie);

        // 4️⃣ Dodanie sali kinowej
        final CinemaHall hall = new CinemaHall();
        hall.setCapacity(100);
        hall.setDescription("Hall 1");
        cinemaHallService.add(hall);

        // 5️⃣ Utworzenie sesji filmowej
        final MovieSession tomorrowSession = new MovieSession();
        tomorrowSession.setMovie(movie);
        tomorrowSession.setCinemaHall(hall);
        tomorrowSession.setShowTime(LocalDateTime.now().plusDays(1));
        movieSessionService.add(tomorrowSession);

        // 6️⃣ Dodanie biletu do koszyka
        shoppingCartService.addSession(tomorrowSession, user);

        // 7️⃣ Wyświetlenie koszyka
        final ShoppingCart cart = shoppingCartService.getByUser(user);
        System.out.println("Koszyk po dodaniu biletu: " + cart);

        // 8️⃣ Wyczyszczenie koszyka
        shoppingCartService.clear(cart);
        System.out.println("Koszyk po wyczyszczeniu: " + shoppingCartService.getByUser(user));
    }
}
