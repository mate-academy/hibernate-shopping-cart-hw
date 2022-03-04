package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import mate.academy.dao.TicketDao;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;

public class Main {
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");
        MovieService movieService = (MovieService) injector
                .getInstance(MovieService.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        CinemaHallService cinemaHallService = (CinemaHallService) injector
                .getInstance(CinemaHallService.class);
        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        MovieSessionService movieSessionService = (MovieSessionService) injector
                .getInstance(MovieSessionService.class);
        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));

        System.out.println("==========New tests==========");

        AuthenticationService authenticationService = (AuthenticationService) injector
                .getInstance(AuthenticationService.class);
        User bohdan;
        try {
            Ticket firstTicket = new Ticket();
            authenticationService.register("bohdan@gmail.com", "1234");
            bohdan = authenticationService.login("bohdan@gmail.com", "1234");
            firstTicket.setUser(bohdan);
            System.out.println("Added ticket: " + firstTicket + " to DB");
        } catch (RegistrationException | AuthenticationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        ShoppingCartService shoppingCartService = (ShoppingCartService) injector
                .getInstance(ShoppingCartService.class);
        System.out.println("Registering new shopping cart for user: "
                + bohdan);
        shoppingCartService.registerNewShoppingCart(bohdan);

        System.out.println("Adding new ticket with movie session: "
                + tomorrowMovieSession);
        shoppingCartService.addSession(tomorrowMovieSession, bohdan);

        System.out.println("Getting shopping cart for user: " + bohdan);
        ShoppingCart bohdanCart = shoppingCartService.getByUser(bohdan);
        System.out.println(bohdanCart);

        System.out.println("Clearing shopping cart");
        shoppingCartService.clear(bohdanCart);

        System.out.println("Getting shopping cart for user: " + bohdan);
        bohdanCart = shoppingCartService.getByUser(bohdan);
        System.out.println(bohdanCart);

    }
}
