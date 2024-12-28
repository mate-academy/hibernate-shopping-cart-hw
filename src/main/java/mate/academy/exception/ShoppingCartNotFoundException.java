package mate.academy.exception;

public class ShoppingCartNotFoundException extends RuntimeException {
    public ShoppingCartNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShoppingCartNotFoundException(String message) {
        super(message);
    }
}
