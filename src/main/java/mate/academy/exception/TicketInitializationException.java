package mate.academy.exception;

public class TicketInitializationException extends RuntimeException {
    public TicketInitializationException(String message) {
        super(message);
    }

    public TicketInitializationException(String message, Throwable e) {
        super(message, e);
    }
}
