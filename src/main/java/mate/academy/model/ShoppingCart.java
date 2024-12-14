package mate.academy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    private Long id;
    @OneToMany
    private List<Ticket> tickets;
    @OneToOne
    @MapsId
    private User user;

    public ShoppingCart() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "ShoppingCart{"
                + "id=" + id
                + ", tickets= " + tickets
                + ", user =" + user
                + '}';

    }
}
