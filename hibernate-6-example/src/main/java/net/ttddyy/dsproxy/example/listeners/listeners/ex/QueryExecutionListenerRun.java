package net.ttddyy.dsproxy.example.listeners.listeners.ex;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.example.listeners.persist.UserPersist;
import net.ttddyy.dsproxy.example.listeners.utils.ProxyDataSourceUtils;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

import javax.sql.DataSource;
import java.util.List;

public class QueryExecutionListenerRun {
    static class QueryExecutionListenerEx implements QueryExecutionListener {
        @Override
        public void beforeQuery(ExecutionInfo executionInfo, List<QueryInfo> list) {
//            System.out.println(executionInfo);
//            System.out.println(list);
            System.out.println("query before executed");
        }

        @Override
        public void afterQuery(ExecutionInfo executionInfo, List<QueryInfo> list) {
            System.out.println("query after executed");
        }


    }

    public static void main(String[] args) {
        DataSource proxy = getDataSource();
        UserPersist userPersist = new UserPersist();
        userPersist.insertSampleUsers(proxy);
    }


    private static DataSource getDataSource() {
        ProxyDataSourceBuilder builder = ProxyDataSourceUtils.getDefaultProxyDataSourceBuilder();
        QueryExecutionListenerEx listener = new QueryExecutionListenerEx();
        builder.listener(listener);
        return builder.build();
    }
}


