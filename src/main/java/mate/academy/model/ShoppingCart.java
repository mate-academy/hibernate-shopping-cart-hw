package mate.academy.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.annotations.Cascade;

@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany (fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Ticket> tickets;

    @OneToOne(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
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
                + "id="
                + id
                + ", tickets="
                + tickets
                + ", user="
                + user
                + '}';
    }
}
