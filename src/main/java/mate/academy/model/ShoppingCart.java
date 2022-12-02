package mate.academy.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany
    @JoinTable(name = "shoppingCart_ticket",
            joinColumns = @JoinColumn(name = "shoppingCart_user_id"),
            inverseJoinColumns = @JoinColumn(name = "tickets_id"))
    private List<Ticket> tickets;
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    public ShoppingCart(List<Ticket> tickets, User user) {
        this.tickets = tickets;
        this.user = user;
    }
}
