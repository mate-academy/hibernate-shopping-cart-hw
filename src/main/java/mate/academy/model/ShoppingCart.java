package mate.academy.model;

import java.util.List;
import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToOne;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@Table(name = "shoping_carts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany
    private List<Ticket> tickets;
    @OneToOne
    private User user;

    public ShoppingCart() {
    }

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
    public int hashCode() {
        return Objects.hash(id, tickets, user);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ShoppingCart shoppingCart = (ShoppingCart) obj;
        return Objects.equals(id, shoppingCart.id)
                && Objects.equals(tickets, shoppingCart.tickets)
                && Objects.equals(user, shoppingCart.user);
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
