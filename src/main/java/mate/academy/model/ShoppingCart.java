package mate.academy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    private Long id;
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();
    @OneToOne
    @MapsId
    private User user;

    public Long getId() {
        return id;
    }

    public ShoppingCart setId(Long id) {
        this.id = id;
        return this;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public ShoppingCart setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
        return this;
    }

    public User getUser() {
        return user;
    }

    public ShoppingCart setUser(User user) {
        this.user = user;
        return this;
    }
}
