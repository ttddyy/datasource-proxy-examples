package net.ttddyy.dsproxy.example.jndi;

import net.ttddyy.dsproxy.QueryCount;
import net.ttddyy.dsproxy.QueryCountHolder;
import net.ttddyy.dsproxy.listener.logging.CommonsLogLevel;
import net.ttddyy.dsproxy.support.CommonsQueryCountLoggingServletFilter;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import net.ttddyy.dsproxy.support.QueryCountLoggerBuilder;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * Use embedded tomcat and register datasource as JNDI resources.
 *
 * @author Tadaya Tsuyukubo
 */
@SpringBootApplication
@RestController
public class JndiEmbeddedApplication {

    /**
     * To initialize application when it is deployed to web container as a war file.
     */
    public static class ApplicationServletInitializer extends SpringBootServletInitializer {
        @Override
        protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
            return application.sources(JndiEmbeddedApplication.class);
        }
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(JndiEmbeddedApplication.class, args);
    }


    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(Tomcat tomcat) {
                tomcat.enableNaming();
                TomcatEmbeddedServletContainer container = super.getTomcatEmbeddedServletContainer(tomcat);

                // JNDI lookup with embedded tomcat:  https://github.com/spring-projects/spring-boot/issues/2308
                Context context = (Context) container.getTomcat().getHost().findChild("");
                Thread.currentThread().setContextClassLoader(context.getLoader().getClassLoader());
                return container;
            }
        };

        // add JNDI datasource resources
        factory.addContextCustomizers((context) -> {

            // actual datasource
            ContextResource resource = new ContextResource();
            resource.setName("jdbc/ds");
            resource.setType(DataSource.class.getName());
            resource.setProperty("driverClassName", "org.hsqldb.jdbc.JDBCDriver");
            resource.setProperty("url", "jdbc:hsqldb:mem:aname");
            context.getNamingResources().addResource(resource);

            // ProxyDataSource
            ContextResource dspResource = new ContextResource();
            dspResource.setName("my/proxy");
            dspResource.setType(ProxyDataSource.class.getName());
            dspResource.setProperty("factory", "net.ttddyy.dsproxy.support.jndi.ProxyDataSourceObjectFactory");
            dspResource.setProperty("dataSource", "java:comp/env/jdbc/ds");  // reference actual datasource
            dspResource.setProperty("proxyName", "MyProxy");
            dspResource.setProperty("listeners", "commons,count");
            dspResource.setProperty("logLevel", "info");
            context.getNamingResources().addResource(dspResource);

        });

        return factory;
    }

    @Bean
    public DataSource dataSource() throws NamingException {
        // look up JNDI for this application
        JndiObjectFactoryBean factory = new JndiObjectFactoryBean();
        factory.setJndiName("java:comp/env/my/proxy");  // lookup ProxyDataSource
        factory.setExpectedType(DataSource.class);  // as javax.sql.DataSource
        factory.setLookupOnStartup(false);  // deffer the lookup because resources get registered at app startup
        factory.afterPropertiesSet();
        return (DataSource) factory.getObject();
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {
            // populate DB at app startup
            jdbcTemplate.execute("CREATE TABLE users(id INT, name VARCHAR(255))");
            jdbcTemplate.batchUpdate("INSERT INTO users (id, name) VALUES (?, ?)",
                    Arrays.asList(new Object[]{1, "foo"}, new Object[]{2, "bar"}));
        };
    }

    @Bean
    public CommonsQueryCountLoggingServletFilter commonsQueryCountLoggingServletFilter() {
        // servlet-filter bean
        return QueryCountLoggerBuilder.create().buildCommonsFilter(CommonsLogLevel.INFO);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping
    public ModelMap index() throws Exception {
        int numOfUsers = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);

        QueryCount queryCount = QueryCountHolder.get("MyProxy");

        ModelMap result = new ModelMap();
        result.put("dataSourceName", "MyProxy");
        result.put("queryCount", queryCount);
        result.put("numOfUsers", numOfUsers);
        return result;
    }
}
