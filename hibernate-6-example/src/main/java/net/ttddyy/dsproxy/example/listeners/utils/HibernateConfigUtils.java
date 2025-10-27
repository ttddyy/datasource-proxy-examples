package net.ttddyy.dsproxy.example.listeners.utils;

import net.ttddyy.dsproxy.example.listeners.entity.UserTable;
import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;
import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class HibernateConfigUtils {
    private static SessionFactory sessionFactory;

    private HibernateConfigUtils() throws IllegalAccessException {
        throw new IllegalAccessException("This is utility method, cannot create object");
    }

    public static synchronized SessionFactory getSessionFactory(DataSource dataSource) {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory(dataSource);
        }
        return sessionFactory;
    }

    public static void closeSessionFactory(DataSource dataSource) {
        getSessionFactory(dataSource).close();
    }

    private static SessionFactory buildSessionFactory(DataSource dataSource) {
        StandardServiceRegistry standardRegistry = null;
        try {
            StandardServiceRegistryBuilder standardRegistryBuilder = new StandardServiceRegistryBuilder();
            Map<String, Object> settings = new HashMap<>();
            settings.put(Environment.DATASOURCE, dataSource);
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
                    .addAnnotatedClass(UserTable.class);

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

    //use hibernate to format queries
    private static class PrettyQueryEntryCreator extends DefaultQueryLogEntryCreator {
        private final Formatter formatter = FormatStyle.BASIC.getFormatter();

        @Override
        protected String formatQuery(String query) {
            return this.formatter.format(query);
        }
    }

    public static DataSource getH2DataSource() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("");
        return ds;
    }
}
