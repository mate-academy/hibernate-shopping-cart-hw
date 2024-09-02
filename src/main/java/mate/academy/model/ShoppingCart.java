package mate.academy.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id")
    private List<Ticket> tickets;
    private User uses;
}
