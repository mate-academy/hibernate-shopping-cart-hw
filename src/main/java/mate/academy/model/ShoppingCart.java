package mate.academy.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(
            mappedBy = "shoppingCart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Ticket> tickets = new ArrayList<>();

    @OneToOne(mappedBy = "shoppingCart")
    private User user;

    public ShoppingCart() {
    }

    public ShoppingCart(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public User getUser() {
        return user;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setShoppingCart(this);
    }

    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticket.setShoppingCart(null);
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ShoppingCart{"
                + "id=" + id
                + ", ticketsCount=" + (tickets != null ? tickets.size() : 0)
                + ", userId=" + (user != null ? user.getId() : null)
                + '}';
    }
}

