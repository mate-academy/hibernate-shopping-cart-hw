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
import mate.academy.service.UserService;

public class Main {
    private static final Injector INJECTOR
            = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        MovieService movieService = (MovieService) INJECTOR.getInstance(MovieService.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        Movie bond007 = new Movie("James Bond 007");
        bond007.setDescription("The James Bond series focuses on James Bond, "
                + "a fictional British Secret Service agent created in 1953 by writer "
                + "Ian Fleming, who featured him in twelve ...");
        movieService.add(bond007);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        CinemaHall vipCinemaHall = new CinemaHall();
        vipCinemaHall.setCapacity(10);
        vipCinemaHall.setDescription("Vip Hall with capacity 10");

        CinemaHallService cinemaHallService = (CinemaHallService) INJECTOR
                .getInstance(CinemaHallService.class);
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);
        cinemaHallService.add(vipCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession todayMovieSession = new MovieSession();
        todayMovieSession.setCinemaHall(vipCinemaHall);
        todayMovieSession.setMovie(bond007);
        todayMovieSession.setShowTime(LocalDateTime.now());

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSessionService movieSessionService = (MovieSessionService) INJECTOR
                .getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);
        movieSessionService.add(todayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                bond007.getId(), LocalDate.now()));

        System.out.println("*** *** ***");

        AuthenticationService authenticationService
                = (AuthenticationService) INJECTOR.getInstance(AuthenticationService.class);
        authenticationService.register("andrew@gmail.com", "Pass1234");
        authenticationService.register("kate@gmail.com", "Pass1234");

        UserService userService = (UserService) INJECTOR.getInstance(UserService.class);
        User kate = userService.findByEmail("kate@gmail.com").get();

        ShoppingCartService shoppingCartService
                = (ShoppingCartService) INJECTOR.getInstance(ShoppingCartService.class);

        System.out.println(shoppingCartService.getByUser(kate));

        shoppingCartService.addSession(todayMovieSession, kate);
        ShoppingCart byUser = shoppingCartService.getByUser(kate);
        System.out.println(byUser);

        shoppingCartService.addSession(yesterdayMovieSession, kate);
        System.out.println(shoppingCartService.getByUser(kate));

        shoppingCartService.clear(byUser);
        System.out.println(byUser);
    }
}
