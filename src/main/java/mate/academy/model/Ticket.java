package mate.academy.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private MovieSession movieSession;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public void setId(Long id) {
        this.id = id;
    }

    public void setMovieSession(MovieSession movieSession) {
        this.movieSession = movieSession;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public MovieSession getMovieSession() {
        return movieSession;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Ticket{"
                + "id=" + id
                + ", movieSession=" + movieSession
                + ", user=" + user
                + '}';
    }
}
