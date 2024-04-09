package mate.academy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MovieSession movieSessions;

    @ManyToOne
    private User user;

    public Ticket() {
    }

    public Ticket(MovieSession movieSessions, User user) {
        this.movieSessions = movieSessions;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovieSession getMovieSessions() {
        return movieSessions;
    }

    public void setMovieSessions(MovieSession movieSessions) {
        this.movieSessions = movieSessions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Ticket{"
                + "id=" + id
                + ", movieSessions=" + movieSessions
                + ", user=" + user
                + '}';
    }
}
