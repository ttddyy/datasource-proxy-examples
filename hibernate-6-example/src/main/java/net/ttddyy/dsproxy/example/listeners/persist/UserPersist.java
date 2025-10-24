package net.ttddyy.dsproxy.example.listeners.persist;

import net.ttddyy.dsproxy.example.listeners.entity.UserTable;
import net.ttddyy.dsproxy.example.listeners.utils.HibernateConfigUtils;
import org.hibernate.Session;

import javax.sql.DataSource;


public class UserPersist {

    // Perform sample database operations
    public void insertSampleUsers(DataSource dataSource) {
        try (Session session = HibernateConfigUtils.getSessionFactory(dataSource).getCurrentSession()) {
            session.getTransaction().begin();
            session.persist(new UserTable(1, "foo"));
            session.persist(new UserTable(2, "bar"));
            session.persist(new UserTable(3, "tar"));
            session.getTransaction().commit();
        } finally {
            HibernateConfigUtils.closeSessionFactory(dataSource);
        }
    }

}
