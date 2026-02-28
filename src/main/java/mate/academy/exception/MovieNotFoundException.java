package mate.academy.exception;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(Long id) {
        super("Movie with ID " + id + " is not found.");
    }
}
