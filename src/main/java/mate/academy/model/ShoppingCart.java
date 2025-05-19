package mate.academy.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @PrimaryKeyJoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "shoppingCart", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<Ticket> ticketList = new HashSet<>();

    public ShoppingCart() {
    }

    public ShoppingCart(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(Set<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public void addTicket(Ticket ticket) {
        ticketList.add(ticket);
    }

    @Override
    public String toString() {
        return "ShoppingCart{"
                + "id=" + id
                + ", user=" + (user != null ? user.getId() : null)
                + ", ticketList=" + (ticketList != null ? ticketList.stream()
                .map(Ticket::getId).collect(Collectors.toList()) : null)
                + '}';
    }
}
