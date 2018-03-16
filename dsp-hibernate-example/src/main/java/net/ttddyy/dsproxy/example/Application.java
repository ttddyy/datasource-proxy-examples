package net.ttddyy.dsproxy.example;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;

import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;
import net.ttddyy.dsproxy.listener.logging.SystemOutQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

public class Application {

    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            // Insert into table
            session.getTransaction().begin();
            Usertable fooUser = new Usertable();
            fooUser.setId(1);
            fooUser.setName("foo");
            session.save(fooUser);
            Usertable barUser = new Usertable();
            barUser.setId(2);
            barUser.setName("bar");
            session.save(barUser);
            Usertable tarUser = new Usertable();
            tarUser.setId(3);
            tarUser.setName("tar");
            session.save(tarUser);
            session.getTransaction().commit();
        } finally {
            HibernateUtil.closeSessionFactory();
        }
    }

    // use hibernate to format queries
    private static class PrettyQueryEntryCreator extends DefaultQueryLogEntryCreator {
        private Formatter formatter = FormatStyle.BASIC.getFormatter();

        @Override
        protected String formatQuery(String query) {
            return this.formatter.format(query);
        }
    }

    @Entity
    static class Usertable implements Serializable {
        private static final long serialVersionUID = 1L;

        @Id
        private int id;
        @Column(nullable = false)
        private String name;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class HibernateUtil {
        private static SessionFactory sessionFactory;
        
        private HibernateUtil() throws IllegalAccessException {
            throw new IllegalAccessException("This is utility method, cannot create object");
        }
        
        public static synchronized SessionFactory getSessionFactory() {
            if (sessionFactory == null) {
                sessionFactory = buildSessionFactory();
            }
            return sessionFactory;
        }

        public static void closeSessionFactory() {
            getSessionFactory().close();
        }

        private static SessionFactory buildSessionFactory() {
            StandardServiceRegistry standardRegistry = null;
            try {
                StandardServiceRegistryBuilder standardRegistryBuilder = new StandardServiceRegistryBuilder();
                Map<String, Object> settings = new HashMap<>();
                settings.put(Environment.DATASOURCE, getDataSource());
                settings.put(Environment.HBM2DDL_AUTO, "create-drop");
                settings.put(Environment.ORDER_UPDATES, true);
                settings.put(Environment.ORDER_INSERTS, true);
                settings.put(Environment.STATEMENT_BATCH_SIZE, 10);
                settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                standardRegistryBuilder.applySettings(settings);
                standardRegistry = standardRegistryBuilder.build();

                // builds a session factory from the service registry
                MetadataSources metadataSources = new MetadataSources(standardRegistry)
                        .addAnnotatedClass(Usertable.class);

                Metadata metadata = metadataSources.getMetadataBuilder().build();
                return metadata.buildSessionFactory();
            } catch (Exception ex) {
                // Make sure you log the exception, as it might be swallowed
                System.err.println(
                        "Initial SessionFactory creation failed." + ex.getMessage());
                // The registry would be destroyed by the SessionFactory, but we had
                // trouble
                // building the SessionFactory so destroy it manually.
                StandardServiceRegistryBuilder.destroy(standardRegistry);
                throw new ExceptionInInitializerError(ex.getCause());
            }
        }
        
        private static DataSource getDataSource() {
            // use pretty formatted query with multiline enabled
            PrettyQueryEntryCreator creator = new PrettyQueryEntryCreator();
            creator.setMultiline(true);
            SystemOutQueryLoggingListener listener = new SystemOutQueryLoggingListener();
            listener.setQueryLogEntryCreator(creator);

            // Create ProxyDataSource
            return ProxyDataSourceBuilder.create(getH2DataSource())
                    .name("ProxyDataSource")
                    .countQuery()
                    .multiline()
                    .listener(listener)
                    .logSlowQueryToSysOut(1, TimeUnit.MINUTES)
                    .build();
        }

        private static DataSource getH2DataSource() {
            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
            ds.setUser("sa");
            ds.setPassword("");
            return ds;
        }
    }

}
