package mate.academy.service.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        if (shoppingCartDao.getByUser(user).isPresent()) {
            ShoppingCart shoppingCart = shoppingCartDao.getByUser(user).get();
            Ticket newTicket = ticketDao.add(new Ticket(movieSession, user));
            List<Ticket> newListTicket = shoppingCart.getTickets();
            newListTicket.add(newTicket);
            shoppingCart.setTickets(newListTicket);
            shoppingCartDao.update(shoppingCart);
        } else {
            throw new RuntimeException("Can't add session");
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ShoppingCart> scCriteriaQuery = cb.createQuery(ShoppingCart.class);
            Root<ShoppingCart> cartRoot = scCriteriaQuery.from(ShoppingCart.class);
            Predicate getByUserPredicate = cb.equal(cartRoot.get("user_id"), user.getId());
            scCriteriaQuery.where(getByUserPredicate);
            return session.createQuery(scCriteriaQuery).getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("", e);
        }
    }

    @Override
    public void registerNewShoppingCart(User user) {
        shoppingCartDao.add(new ShoppingCart(user, new ArrayList<Ticket>()));
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(new ArrayList<Ticket>());
        shoppingCartDao.update(shoppingCart);
    }
}
