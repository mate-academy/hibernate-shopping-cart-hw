package mate.academy.model;

import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "shopping_carts_tickets",
    joinColumns = @JoinColumn(name = "shopping_cart_id"),
    inverseJoinColumns = @JoinColumn(name = "ticket_id"))
    private List<Ticket> tickets;

    public Long getId() {
        return id;
    }

    public ShoppingCart() {
    }

    public ShoppingCart(User user, List<Ticket> tickets) {
        this.user = user;
        this.tickets = tickets;
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
