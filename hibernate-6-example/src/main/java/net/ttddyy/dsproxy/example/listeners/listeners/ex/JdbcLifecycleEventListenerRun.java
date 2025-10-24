package net.ttddyy.dsproxy.example.listeners.listeners.ex;

import net.ttddyy.dsproxy.example.listeners.persist.UserPersist;
import net.ttddyy.dsproxy.example.listeners.utils.ProxyDataSourceUtils;
import net.ttddyy.dsproxy.listener.MethodExecutionContext;
import net.ttddyy.dsproxy.listener.lifecycle.JdbcLifecycleEventListenerAdapter;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

import javax.sql.DataSource;

public class JdbcLifecycleEventListenerRun {
    static class JdbcLifecycleEventListenerEx extends JdbcLifecycleEventListenerAdapter {

        // impl for before DataSource#getConnection();
        @Override
        public void beforeGetConnection(MethodExecutionContext executionContext) {
            System.out.println("beforeGetConnection executed");
        }

        // impl for after Connection#rollback();
        @Override
        public void afterRollback(MethodExecutionContext executionContext) {
            System.out.println("afterRollback executed");
        }
    }

    public static void main(String[] args) {
        DataSource proxy = getDataSource();
        UserPersist userPersist = new UserPersist();
        userPersist.insertSampleUsers(proxy);
    }


    private static DataSource getDataSource() {
        ProxyDataSourceBuilder builder = ProxyDataSourceUtils.getDefaultProxyDataSourceBuilder();
        JdbcLifecycleEventListenerEx listener = new JdbcLifecycleEventListenerEx();
        builder.listener(listener);
        return builder.build();
    }
}

