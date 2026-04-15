package mate.academy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "movie_session_id", nullable = false)
    private MovieSession movieSession;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Ticket() {
    }

    public Long getId() {
        return id;
    }

    public Ticket setId(Long id) {
        this.id = id;
        return this;
    }

    public MovieSession getMovieSession() {
        return movieSession;
    }

    public Ticket setMovieSession(MovieSession movieSession) {
        this.movieSession = movieSession;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Ticket setUser(User user) {
        this.user = user;
        return this;
    }
}
