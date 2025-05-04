package mate.academy.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;
    @OneToMany
    @JoinTable(name = "shopping_carts_tickets",
            joinColumns = @JoinColumn(name = "shopping_carts_id"),
            inverseJoinColumns = @JoinColumn(name = "ticket_id"))
    private List<Ticket> tickets;

    public ShoppingCart() {
    }

    public ShoppingCart(User user) {
        this.user = user;
        tickets = new ArrayList<>();
    }

    public ShoppingCart(User user, List<Ticket> tickets) {
        this.user = user;
        this.tickets = tickets;
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

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "ShoppingCart{"
                + "id=" + id
                + ", user=" + user
                + ", tickets=" + tickets
                + '}';
    }
}
