package mate.academy.exception;

import org.hibernate.exception.ConstraintViolationException;

public class AuthenticationException extends Exception {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, ConstraintViolationException exception) {
        super(message, exception);
    }
}
