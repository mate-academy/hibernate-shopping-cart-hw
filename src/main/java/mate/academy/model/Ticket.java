package mate.academy.model;

public class Ticket {
    private Long id;
    private User user;
    private MovieSession movieSession;

    public Ticket() {
    }

    public Ticket(User user, MovieSession movieSession) {
        this.user = user;
        this.movieSession = movieSession;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MovieSession getMovieSession() {
        return movieSession;
    }

    public void setMovieSession(MovieSession movieSession) {
        this.movieSession = movieSession;
    }

    @Override
    public String toString() {
        return "Ticket{"
                + "id=" + id
                + ", user=" + user
                + ", movieSession=" + movieSession
                + '}';
    }
}
