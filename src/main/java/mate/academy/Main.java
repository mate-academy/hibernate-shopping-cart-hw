package mate.academy;

import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.MovieSession;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

public class Main {
    public static void main(String[] args) throws AuthenticationException, RegistrationException {
        Injector injector = Injector.getInstance("mate.academy");

        User user = new User();
        user.setEmail("ihor@ukr.net");
        user.setSalt(HashUtil.getSalt());
        user.setPassword(HashUtil.hashPassword("12345", user.getSalt()));

        AuthenticationService authenticationService
                = (AuthenticationService) injector.getInstance(AuthenticationService.class);
        System.out.println(authenticationService.register("ihor@ukr.net", "12345"));

        MovieSessionService movieSessionService
                = (MovieSessionService) injector.getInstance(MovieSessionService.class);
        MovieSession movieSession = movieSessionService.get(4L);
        UserService userService = (UserService) injector.getInstance(UserService.class);
        User userFromDB = userService.findByEmail("ihor@ukr.net").get();
        ShoppingCartService shoppingCartService
                = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        shoppingCartService.addSession(movieSession, userFromDB);

        shoppingCartService
                .clear(shoppingCartService
                        .getByUser(userService.findByEmail("ihor@ukr.net").get()));

    }
}
