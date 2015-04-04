
# About

This project is to demonstrate how to use datasource-proxy from JNDI.


# Setup


The setup is using Tomcat and HSQLDB. 


1. download `hsqldb.jar` and `datasource-proxy.jar`(1.3+) place them to `${CATALINA_HOME}/lib`

1. Configure Tomcat

`${CATALINA_HOME}/conf/server.xml`

```xml
    <Resource name="jdbc/global/myDS" 
              auth="Container"
              type="javax.sql.DataSource"
              description="ds"
              driverClassName="org.hsqldb.jdbc.JDBCDriver"
              url="jdbc:hsqldb:mem:aname"
    />

    <Resource name="jdbc/global/myProxy" 
              auth="Container"
              type="net.ttddyy.dsproxy.support.ProxyDataSource"
              factory="net.ttddyy.dsproxy.support.jndi.ProxyDataSourceObjectFactory"
              description="ds"
              listeners="count,sysout"
              proxyName="DS-PROXY"
              format="json"
              dataSource="java:jdbc/global/myDS"
    />
```

`${CATALINA_HOME}/conf/context.xml`

```xml
    <ResourceLink name="jdbc/myDS"
                  global="jdbc/global/myProxy"
                  type="javax.sql.DataSource"
    />
```

1. build this application and deploy to tomcat
```
> mvn package
// or mvn war:war, etc
```
  * copy war to `${CATALINA_HOME}/webapps`, or symlink, etc


