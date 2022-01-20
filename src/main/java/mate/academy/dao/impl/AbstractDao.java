package mate.academy.dao.impl;

import mate.academy.util.HibernateUtil;
import org.hibernate.SessionFactory;

abstract class AbstractDao {
    protected final SessionFactory factory = HibernateUtil.getSessionFactory();

}
