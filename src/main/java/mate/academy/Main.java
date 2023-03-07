package mate.academy;

import mate.academy.dao.TicketDao;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
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
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) throws RegistrationException {
        final MovieService movieService =
                (MovieService) injector.getInstance(MovieService.class);
        final CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        final MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        final TicketDao ticketDao = (TicketDao) injector.getInstance(TicketDao.class);
        final UserService userService = (UserService) injector.getInstance(UserService.class);
        final AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);

        //        Movie fastAndFurious = new Movie("Fast and Furious");
        //        fastAndFurious.setDescription(
        //        "An action film about street racing, heists, and spies.");
        //       movieService.add(fastAndFurious);
        ////       // System.out.println(movieService.get(fastAndFurious.getId()));
        ////       // movieService.getAll().forEach(System.out::println);
        ////
        //        CinemaHall firstCinemaHall = new CinemaHall();
        //        firstCinemaHall.setCapacity(100);
        //        firstCinemaHall.setDescription("first hall with capacity 100");
        //
        //        CinemaHall secondCinemaHall = new CinemaHall();
        //        secondCinemaHall.setCapacity(200);
        //        secondCinemaHall.setDescription("second hall with capacity 200");
        //
        //       cinemaHallService.add(firstCinemaHall);
        //        cinemaHallService.add(secondCinemaHall);
        ////
        //////        System.out.println(cinemaHallService.getAll());
        //////        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));
        ////
        //        MovieSession tomorrowMovieSession = new MovieSession();
        //        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        //        tomorrowMovieSession.setMovie(fastAndFurious);
        //        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));
        //
        //       MovieSession yesterdayMovieSession = new MovieSession();
        //        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        //        yesterdayMovieSession.setMovie(fastAndFurious);
        //        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));
        //
        //       movieSessionService.add(tomorrowMovieSession);
        //       movieSessionService.add(yesterdayMovieSession);

        ///System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        ////        System.out.println(movieSessionService.findAvailableSessions(
        ////                fastAndFurious.getId(), LocalDate.now()));

        //        Movie movieTerminator = new Movie("Terminator", "Super");
        //        final Movie movieMatix = new Movie("Martix", "Cool");
        //        Movie moviefastAndFurious = new Movie("Fast and Furious");
        //        moviefastAndFurious.setDescription(
        //                "An action film about street racing, heists, and spies.");
        //
        //        movieService.add(moviefastAndFurious);
        //        movieService.add(movieTerminator);
        //        movieService.add(movieMatix);
        //
        //        CinemaHall smallHall = new CinemaHall(100, "Small hall");
        //        CinemaHall midleHall = new CinemaHall(150, "Midl hall");
        //        CinemaHall ladgeHall = new CinemaHall(200, "Ladge");
        //
        //        cinemaHallService.add(smallHall);
        //        cinemaHallService.add(midleHall);
        //        cinemaHallService.add(ladgeHall);
        //
        //        MovieSession movieSession1 = new MovieSession(movieTerminator, smallHall,
        //                LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 30, 0)));
        //        MovieSession movieSession2 = new MovieSession(movieTerminator, midleHall,
        //                LocalDateTime.of(LocalDate.now(), LocalTime.of(5, 30, 0)));
        //        MovieSession movieSession3 = new MovieSession(movieTerminator, ladgeHall,
        //                LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 30, 0)));
        //        MovieSession movieSession4 = new MovieSession(movieTerminator, ladgeHall,
        //                LocalDateTime.of(LocalDate.of(2023, 4, 2), LocalTime.of(11, 30, 0)));
        //        MovieSession movieSession5 = new MovieSession(movieTerminator, ladgeHall,
        //                LocalDateTime.of(LocalDate.of(2023, 2, 22), LocalTime.of(0, 0, 0)));
        //
        //        movieSessionService.add(movieSession1);
        //        movieSessionService.add(movieSession2);
        //        movieSessionService.add(movieSession3);
        //        movieSessionService.add(movieSession4);
        //        movieSessionService.add(movieSession5);
        //
        //        User bob = new User("bob@gmail", "qwery");
        //        bob.setSalt(HashUtil.getSalt());
        //        bob.setPassword(HashUtil.hashPassword("qwery", bob.getSalt()));
        //
        //        User alisa = new User("alise@gmail", "12345");
        //        bob.setSalt(HashUtil.getSalt());
        //        bob.setPassword(HashUtil.hashPassword("12345", bob.getSalt()));
        //
        //        User john = new User("john@gmail", "helloword");
        //        bob.setSalt(HashUtil.getSalt());
        //        bob.setPassword(HashUtil.hashPassword("helloword", bob.getSalt()));
        //                userService.add(bob);
        //                userService.add(alisa);
        //            userService.add(john);

        //Ticket ticket1 = new Ticket(userService.findByEmail(
        // "bob@gmail").get(), movieSessionService.get(1L));
        //  Ticket ticket2 = new Ticket(userService.findByEmail(
        //  "alise@gmail").get(), movieSessionService.get(2L));

        //     ticketDao.add(ticket1);
        //       ticketDao.add(ticket2);

        // ticketDao.getAll().forEach(x-> System.out.println(x));

        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        // User userBobFromDB = userService.findByEmail("john@gmail").get();
        //shoppingCartService.registerNewShoppingCart(userBobFromDB);
        //  ShoppingCart bobsShoppingCart = shoppingCartService.getByUser(userBobFromDB);

        // System.out.println(bobsShoppingCart);

        //shoppingCartService.addSession(movieSessionService.get(5L), userBobFromDB);
        // shoppingCartService.addSession(movieSessionService.get(3L), userBobFromDB);
        // bobsShoppingCart.getTickets().forEach(x-> System.out.println(x));

        // authenticationService.register("pony@gmail","ldjflsjf");
        User pony = userService.findByEmail("pony@gmail").get();
        System.out.println(pony);
        ShoppingCart ponyShoppingCart = shoppingCartService.getByUser(pony);
        System.out.println(ponyShoppingCart);
        MovieSession movieSession1 = movieSessionService.get(2L);
        // shoppingCartService.addSession(movieSession1,pony);
        System.out.println(ponyShoppingCart);
    }
}
