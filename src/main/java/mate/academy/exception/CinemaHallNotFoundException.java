package mate.academy.exception;

public class CinemaHallNotFoundException extends RuntimeException {
    public CinemaHallNotFoundException(Long id) {
        super("Cinema hall with ID " + id + " is not found");
    }
}
