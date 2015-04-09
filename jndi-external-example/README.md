
# About

This project demonstrates how to use datasource-proxy from JNDI using Tomcat.

* If `ProxyDataSource` is configued as a JNDI resource and passed to the application as `javax.sql.DataSource`,
then only container needs to know `datasource-proxy.jar`. Your application doesn't need a dependency to `datsource-proxy.jar`.

# Setup

Setup uses Tomcat and HSQLDB. 


1. Download `hsqldb.jar` and `datasource-proxy.jar`(1.3+) place them to `${CATALINA_HOME}/lib`

1. Configure Tomcat

  * `${CATALINA_HOME}/conf/server.xml`

    ```xml

    <!--
      Define actual datasource `jdbc/global/myDS` using embedded hsqldb.
     -->
    <Resource name="jdbc/global/myDS" 
              auth="Container"
              type="javax.sql.DataSource"
              description="ds"
              driverClassName="org.hsqldb.jdbc.JDBCDriver"
              url="jdbc:hsqldb:mem:aname"
    />
    
    <!--
      * Define `ProxyDataSource` with `DataSourceQueryCountListener` and `SystemOutQueryLoggingListener`
     -->
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

  * `${CATALINA_HOME}/conf/context.xml`

    ```xml
    <!--
      Expose defined `ProxyDataSource` to application as a `DataSource`
     -->
    <ResourceLink name="jdbc/myDS"
                  global="jdbc/global/myProxy"
                  type="javax.sql.DataSource"
    />
    ```

1. build this application and deploy to tomcat

  ```shell
  > mvn package
  // or mvn war:war, etc
  ```

  * copy war to `${CATALINA_HOME}/webapps`, or symlink, etc

1. start tomcat and access the url

---

**Sample Output**


Application startup log:

```
{"name":"DS-PROXY", "time":1, "success":true, "type":"Statement", "batch":false, "querySize":1, "batchSize":0, "query":["CREATE TABLE users(id INT, name VARCHAR(255))"], "params":[]}
{"name":"DS-PROXY", "time":0, "success":true, "type":"Prepared", "batch":true, "querySize":1, "batchSize":3, "query":["INSERT INTO users (id, name) VALUES (?, ?)"], "params":[{"1":"1","2":"foo"},{"1":"2","2":"bar"},{"1":"3","2":"baz"}]}
```

Accessing application:

```shell
> curl http://localhost:8080/dsp-lookup/
  {"count":3}
```

Log:
```
{"name":"DS-PROXY", "time":6, "success":true, "type":"Statement", "batch":false, "querySize":1, "batchSize":0, "query":["SELECT COUNT(*) FROM users"], "params":[]}
```
