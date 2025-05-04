package mate.academy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "shopping_cart")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Ticket> ticketList = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ShoppingCart() {

    }

    public Long getId() {
        return id;
    }

    public ShoppingCart setId(Long id) {
        this.id = id;
        return this;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public ShoppingCart setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
        return this;
    }

    public User getUser() {
        return user;
    }

    public ShoppingCart setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public String toString() {
        return "ShoppingCart{"
                + "id=" + id
                + ", ticketList=" + ticketList
                + ", user=" + user
                + '}';
    }
}
