package mate.academy.dao.impl;

import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

@Dao
public class TicketDaoImpl implements TicketDao {
    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Override
    public Ticket add(Ticket ticket) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(ticket);
            transaction.commit();
            return ticket;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert ticket " + ticket, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

//    @Override
//    public Ticket add(Ticket ticket) {
//        Transaction transaction = null;
//        Session session = null;
//        try {
//            session = HibernateUtil.getSessionFactory().openSession();
//            transaction = session.beginTransaction();
//
//            Optional<ShoppingCart> shoppingCart = shoppingCartDao.getByUser(ticket.getUser());
//            if (shoppingCart.isPresent()) {
//                shoppingCart.get().getTickets().add(ticket);
//                shoppingCartDao.update(shoppingCart.get());
//                session.persist(ticket);
//                transaction.commit();
//                return ticket;
//            }
//            throw new DataProcessingException("Can't find shopping cart by user: " + ticket.getUser());
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            throw new DataProcessingException("Can't insert ticket " + ticket, e);
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//    }
}
