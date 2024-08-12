package mate.academy;

import java.time.LocalDateTime;
import mate.academy.lib.Injector;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.MovieSessionService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final ShoppingCartService shoppingCartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
    private static final MovieSessionService movieSessionService =
            (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static final UserService userService =
            (UserService) injector.getInstance(UserService.class);

    public static void main(String[] args) {
        // Створюємо та зберігаємо користувача, якщо його ще немає в базі даних
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("qwerty");
        user = userService.add(user); // Зберігаємо користувача

        MovieSession movieSession = new MovieSession();
        movieSession.setShowTime(LocalDateTime.now());

        // Зберігаємо MovieSession перед тим, як додати його до квитка
        movieSession = movieSessionService.add(movieSession);

        // Реєструємо новий кошик для користувача
        shoppingCartService.registerNewShoppingCart(user);

        // Додаємо сеанс до кошика покупок
        shoppingCartService.addSession(movieSession, user);

        // Отримуємо оновлений кошик покупок користувача
        ShoppingCart shoppingCart = shoppingCartService.getByUser(user);
        System.out.println("Shopping Cart for user: " + user.getEmail());
        shoppingCart.getTickets().forEach(System.out::println);
    }
}
