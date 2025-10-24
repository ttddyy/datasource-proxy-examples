package net.ttddyy.dsproxy.example.listeners.listeners.ex;

import net.ttddyy.dsproxy.example.listeners.persist.UserPersist;
import net.ttddyy.dsproxy.example.listeners.utils.ProxyDataSourceUtils;
import net.ttddyy.dsproxy.listener.MethodExecutionContext;
import net.ttddyy.dsproxy.listener.MethodExecutionListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

import javax.sql.DataSource;

public class MethodExecutionListenerRun {
    static class MethodExecutionListenerEx implements MethodExecutionListener {
        @Override
        public void beforeMethod(MethodExecutionContext methodExecutionContext) {
            System.out.println("beforeMethod executed");
        }

        @Override
        public void afterMethod(MethodExecutionContext methodExecutionContext) {
            System.out.println("afterMethod executed");
        }





    }
    public static void main(String[] args) {
        DataSource proxy = getDataSource();
        UserPersist userPersist = new UserPersist();
        userPersist.insertSampleUsers(proxy);
    }
    private static DataSource getDataSource() {
        ProxyDataSourceBuilder builder = ProxyDataSourceUtils.getDefaultProxyDataSourceBuilder();
        MethodExecutionListenerEx methodExecutionListenerEx = new MethodExecutionListenerEx();
        builder.methodListener(methodExecutionListenerEx);
        return builder.build();
    }
}


