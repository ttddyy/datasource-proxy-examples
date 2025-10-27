package net.ttddyy.dsproxy.example.listeners.listeners.builtIn;

import net.ttddyy.dsproxy.example.listeners.persist.UserPersist;
import net.ttddyy.dsproxy.example.listeners.utils.ProxyDataSourceUtils;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

import javax.sql.DataSource;

/**
 * Demonstrates how to use the Query Logging Listener from datasource-proxy.
 * This listener logs executed SQL queries along with actual parameters to
 * System.out, SLF4J, Commons Logging, or JUL.
 * Features shown:
 * - Logging to System.out
 * - Multiline output
 * - SQL formatting using Hibernate's FormatStyle (v1.9+)
 * - Query filtering (e.g., exclude 'audit' table)
 *
 */
public class QueryLoggingListenerRun {

    public static void main(String[] args) {
        DataSource proxyDataSource = createDataSourceWithQueryLogging();


        UserPersist userPersist = new UserPersist();
        userPersist.insertSampleUsers(proxyDataSource);
    }

    /**
     * Creates a proxy DataSource configured with a query logging listener.
     * This example uses System.out for logging, but other loggers (SLF4J, etc.)
     * can be used by calling corresponding methods like .logQueryBySlf4j().
     *
     * @return a wrapped DataSource that logs all SQL queries
     * <p>
     */
    private static DataSource createDataSourceWithQueryLogging() {
        ProxyDataSourceBuilder builder = ProxyDataSourceUtils.getDefaultProxyDataSourceBuilder();
//        Optional: Format SQL using Hibernate's basic formatter (requires datasource-proxy v1.9+)
//        This makes logged SQL more readable (e.g., adds line breaks and indentation)
//        PrettyQueryEntryCreator creator = new PrettyQueryEntryCreator();
//        creator.setMultiline(true);
//        SystemOutQueryLoggingListener listener = new SystemOutQueryLoggingListener();
//        listener.setQueryLogEntryCreator(creator);

        // Enable query logging to System.out
        builder.logQueryToSysOut();
//        builder.logQueryByCommons();   // log using Commons
//        builder.logQueryBySlf4j();     // log using SLF4J
//        builder.logQueryByJUL() ;      // log using Java Util Logging

        // Optional: Enable multiline output for better readability
        builder.multiline();


        // Build and return the proxy DataSource
        return builder.build();
    }
}
