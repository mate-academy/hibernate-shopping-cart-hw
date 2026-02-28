package mate.academy.exception;

public class MovieSessionNotFoundException extends RuntimeException {
    public MovieSessionNotFoundException(Long id) {
        super("Movie session with ID " + id + " is not found.");
    }
}
