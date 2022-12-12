package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.RegistrationException;
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
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");

        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);

        Movie interstellar = new Movie("Interstellar");
        fastAndFurious.setDescription("scy-fi");
        movieService.add(interstellar);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        CinemaHallService cinemaHallService
                = (CinemaHallService) injector.getInstance(CinemaHallService.class);
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        MovieSession todaySession = new MovieSession();
        todaySession.setCinemaHall(firstCinemaHall);
        todaySession.setMovie(fastAndFurious);
        todaySession.setShowTime(LocalDateTime.now());

        MovieSession afterTomorrowSession = new MovieSession();
        afterTomorrowSession.setCinemaHall(firstCinemaHall);
        afterTomorrowSession.setMovie(fastAndFurious);
        afterTomorrowSession.setShowTime(LocalDateTime.now().plusDays(2L));

        MovieSessionService movieSessionService
                = (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(todaySession);
        movieSessionService.add(afterTomorrowSession);

        System.out.println("\n=== " + movieSessionService.get(afterTomorrowSession.getId()));
        System.out.println("\n=== " + movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        AuthenticationService authenticationService
                = (AuthenticationService) injector.getInstance(AuthenticationService.class);

        User stepan;
        User olena;

        try {
            stepan = authenticationService.register("stepan@gmail.com", "1234");
            olena = authenticationService.register("olena@ukr.net", "4321");
        } catch (RegistrationException e) {
            throw new RuntimeException(e);
        }

        ShoppingCartService shoppingCartService
                = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        shoppingCartService.addSession(afterTomorrowSession, stepan);
        shoppingCartService.addSession(afterTomorrowSession, olena);

        System.out.println("\n=== " + shoppingCartService.getByUser(olena).getTickets());

        shoppingCartService.clear(shoppingCartService.getByUser(olena));

        System.out.println("\nafter clearing of the cart === " + shoppingCartService.getByUser(olena).getTickets());

    }
}
