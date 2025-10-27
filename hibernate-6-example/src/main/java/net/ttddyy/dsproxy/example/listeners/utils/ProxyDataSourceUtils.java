package net.ttddyy.dsproxy.example.listeners.utils;

import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

import javax.sql.DataSource;

import static net.ttddyy.dsproxy.example.listeners.utils.HibernateConfigUtils.getH2DataSource;

public class ProxyDataSourceUtils {

    //building  the default proxy DataSource
    public static ProxyDataSourceBuilder getDefaultProxyDataSourceBuilder() {
        return ProxyDataSourceBuilder.create(getH2DataSource())
                .name("ProxyDataSource")
                .multiline();
    }

    public static DataSource buildDataSource(ProxyDataSourceBuilder builder) {
        return builder.build();
    }
}
