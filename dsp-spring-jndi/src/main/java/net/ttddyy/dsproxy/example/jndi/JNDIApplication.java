package net.ttddyy.dsproxy.example.jndi;

import org.apache.catalina.Container;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @author Tadaya Tsuyukubo
 */
@SpringBootApplication
@RestController
public class JNDIApplication {

    /**
     * To initialize application when it is deployed to web container as a war file.
     */
    public static class ApplicationServletInitializer extends SpringBootServletInitializer {
        @Override
        protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
            return application.sources(JNDIApplication.class);
        }
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(JNDIApplication.class, args);
    }


    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(Tomcat tomcat) {
                tomcat.enableNaming();
                TomcatEmbeddedServletContainer container = super.getTomcatEmbeddedServletContainer(tomcat);

                for (Container child : container.getTomcat().getHost().findChildren()) {
                    if (child instanceof Context) {
                        ClassLoader contextClassLoader =
                                ((Context) child).getLoader().getClassLoader();
                        Thread.currentThread().setContextClassLoader(contextClassLoader);
                        break;
                    }
                }
                return container;
            }
        };
        factory.addContextCustomizers((context) -> {
            ContextResource resource = new ContextResource();
            resource.setName("jdbc/ds");
            resource.setType(DataSource.class.getName());
            resource.setProperty("driverClassName", "org.hsqldb.jdbc.JDBCDriver");
            resource.setProperty("url", "jdbc:hsqldb:mem:aname");
            context.getNamingResources().addResource(resource);
        });

        return factory;
    }

    @Bean
    public JndiObjectFactoryBean jndiObjectFactoryBean() throws NamingException {
        JndiObjectFactoryBean factory = new JndiObjectFactoryBean();
        factory.setJndiName("java:comp/env/jdbc/ds");
        factory.setExpectedType(DataSource.class);
        factory.setLookupOnStartup(false);
        return factory;
    }

    @Autowired
    DataSource ds;

    @Bean
    public CommandLineRunner init() {
        return args -> {

            // populate DB
            jdbcTemplate.execute("CREATE TABLE users(id INT, name VARCHAR(255))");
            jdbcTemplate.batchUpdate("INSERT INTO users (id, name) VALUES (?, ?)",
                    Arrays.asList(new Object[]{1, "foo"}, new Object[]{2, "bar"}));
        };
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping
    public String index() throws Exception {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
        return "ABC" + count;
    }
}
