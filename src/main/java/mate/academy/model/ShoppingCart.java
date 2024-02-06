package mate.academy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import java.util.List;

@Entity
public class ShoppingCart {
    @Id
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Ticket> tickets;

    @OneToOne
    @MapsId
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ShoppingCart{"
                + "id=" + id
                + ", tickets=" + tickets
                + ", user=" + user
                + '}';
    }
}
