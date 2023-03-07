package mate.academy.dao.impl;

import java.util.List;
import java.util.Optional;
import mate.academy.dao.TicketDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Ticket;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class TicketDaoImpl implements TicketDao {

    @Override
    public Ticket add(Ticket ticket) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(ticket);
            transaction.commit();
            return ticket;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert movie session" + ticket, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Ticket> get(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Ticket> getMovieSessionByIdQuery =
                    session.createQuery("SELECT  t FROM Ticket AS t "
                            + " LEFT JOIN FETCH t.user"
                            + " LEFT JOIN FETCH t.movieSession "
                            + "WHERE t.id = :id ", Ticket.class);
            getMovieSessionByIdQuery.setParameter("id", id);
            return getMovieSessionByIdQuery.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get a movie session by id: " + id, e);
        }
    }

    @Override
    public List<Ticket> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query getAllTicketQuery = session.createQuery("SELECT t from Ticket t "
                    + "LEFT JOIN FETCH t.user "
                            + "LEFT JOIN FETCH t.movieSession  ",
                    Ticket.class);
            return getAllTicketQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Cant get all Tickets from db", e);
        }
    }
}
