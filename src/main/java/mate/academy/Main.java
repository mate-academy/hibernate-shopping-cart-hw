package mate.academy;

import mate.academy.dao.MovieSessionDao;
import mate.academy.dao.impl.MovieSessionDaoImpl;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.ShoppingCartService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) throws RegistrationException {
        AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        ShoppingCartService shoppingCartService
                = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        MovieSessionDao movieSessionDao = new MovieSessionDaoImpl();
        User register = authenticationService.register("bob228@gmail.com", "123456");
        shoppingCartService.addSession(movieSessionDao.get(1L).get(), register);
    }
}
