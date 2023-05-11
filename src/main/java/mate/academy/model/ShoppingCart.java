package mate.academy.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "shoppingCarts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinTable(name = "shoppingCarts_tickets",
            joinColumns = @JoinColumn(name = "shoppingCart_id"),
            inverseJoinColumns = @JoinColumn(name = "ticket_id"))
    private List<Ticket> tickets;
    @OneToOne
    @MapsId
    private User user;

    public ShoppingCart() {
    }

    public ShoppingCart(List<Ticket> ticket, User user) {
        this.tickets = ticket;
        this.user = user;
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

    public void setTickets(List<Ticket> ticket) {
        this.tickets = ticket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", ticket=" + tickets +
                ", user=" + user +
                '}';
    }
}
