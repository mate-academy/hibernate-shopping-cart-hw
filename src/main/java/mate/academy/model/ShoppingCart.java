package mate.academy.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Ticket> ticketList;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public ShoppingCart() {
    }

    public ShoppingCart(Long id, List<Ticket> ticketList, User user) {
        this.id = id;
        this.ticketList = ticketList;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
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
                ", ticketList=" + ticketList +
                ", user=" + user +
                '}';
    }
}
