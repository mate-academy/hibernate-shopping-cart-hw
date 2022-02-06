package mate.academy.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "shoppingCarts")
public class ShoppingCart {
    @Id
    private Long id;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "shoppingCart_tickets")
    private List<Ticket> tickets;
    @OneToOne(cascade = CascadeType.ALL)
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
                + ", tickets=" + tickets
                + ", user=" + user
                + '}';
    }
}
