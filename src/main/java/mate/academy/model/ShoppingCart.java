package mate.academy.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @MapsId
    private User user;
    @OneToMany
    private List<Ticket> tickets;

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "ShoppingCart{"
                + " id=" + id
                + ", user=" + user
                + ", tickets=" + tickets
                + '}';
    }
}
