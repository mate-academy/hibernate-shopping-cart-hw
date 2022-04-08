package mate.academy.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    private Long id;
    @OneToMany
    @JoinColumn(name = "user_id")
    List<Ticket> tickets;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    User user;

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
                + "id=" + id +
                ", tickets=" + tickets
                + ", user=" + user
                + '}';
    }
}
