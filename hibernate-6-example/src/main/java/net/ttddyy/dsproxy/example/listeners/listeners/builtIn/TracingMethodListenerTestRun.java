package net.ttddyy.dsproxy.example.listeners.listeners.builtIn;

import net.ttddyy.dsproxy.example.listeners.persist.UserPersist;
import net.ttddyy.dsproxy.example.listeners.utils.ProxyDataSourceUtils;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

import javax.sql.DataSource;

public class TracingMethodListenerTestRun {
    public static void main(String[] args) {
        DataSource proxy = getDataSource();
        UserPersist userPersist = new UserPersist();
        userPersist.insertSampleUsers(proxy);
    }


    private static DataSource getDataSource() {
        ProxyDataSourceBuilder builder = ProxyDataSourceUtils.getDefaultProxyDataSourceBuilder();
        builder.traceMethods();
        return builder.build();
    }

}
