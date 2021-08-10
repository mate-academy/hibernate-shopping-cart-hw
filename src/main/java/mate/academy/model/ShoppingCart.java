package mate.academy.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany
    private List<Ticket> userTicket;
    @OneToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Ticket> getUserTicket() {
        return userTicket;
    }

    public void setUserTicket(List<Ticket> userTicket) {
        this.userTicket = userTicket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShoppingCart that = (ShoppingCart) o;
        return Objects.equals(id, that.id)
                && Objects.equals(userTicket, that.userTicket)
                && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userTicket, user);
    }

    @Override
    public String toString() {
        return "ShoppingCart{"
                + "id=" + id
                + ", userTicket=" + userTicket
                + ", user=" + user
                + '}';
    }
}
