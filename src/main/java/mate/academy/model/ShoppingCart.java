package mate.academy.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "shopping_carts_tickets",
            joinColumns = @JoinColumn(name = "shopping_cart_id"),
            inverseJoinColumns = @JoinColumn(name = "ticket_id"))
    private List<Ticket> tickets;
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    public ShoppingCart() {
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
                + "id="
                + id
                + ", tickets="
                + tickets
                + ", user="
                + user
                + '}';
    }
}
