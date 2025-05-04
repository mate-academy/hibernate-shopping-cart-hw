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
    private MovieSession session;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Ticket() {
    }

    public Ticket(MovieSession session, User user) {
        this.session = session;
        this.user = user;
    }

    public Ticket(Long id, MovieSession session, User user) {
        this(session, user);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovieSession getSession() {
        return session;
    }

    public void setSession(MovieSession session) {
        this.session = session;
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
                + ", session=" + session
                + ", user=" + user + '}';
    }
}
