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
        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

        User pony = userService.findByEmail("pony@gmail").get();
        System.out.println(pony);
        ShoppingCart ponyShoppingCart = shoppingCartService.getByUser(pony);
        System.out.println(ponyShoppingCart);
        MovieSession movieSession1 = movieSessionService.get(2L);
        System.out.println(ponyShoppingCart);
    }
}
