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
    private MovieSession movieSession;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public Ticket(MovieSession movieSession, User user) {
        this.movieSession = movieSession;
        owner = user;
    }

    public Ticket() {

    }

    public MovieSession getMovieSession() {
        return movieSession;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        owner = user;
    }

    public void setMovieSession(MovieSession movieSession) {
        this.movieSession = movieSession;
    }

    @Override
    public String toString() {
        return "Ticket{"
                + "movieSession="
                + movieSession
                + ", owner="
                + owner
                + '}';
    }
}
