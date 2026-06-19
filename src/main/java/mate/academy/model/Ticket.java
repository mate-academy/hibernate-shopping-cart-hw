package mate.academy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private MovieSession movieSession;
    @ManyToOne
    private User user;
    @ManyToOne
    private ShoppingCart shoppingCart;

    public Ticket() {
    }

    public Ticket(MovieSession movieSession, User user) {
        this.movieSession = movieSession;
        this.user = user;
    }
}
