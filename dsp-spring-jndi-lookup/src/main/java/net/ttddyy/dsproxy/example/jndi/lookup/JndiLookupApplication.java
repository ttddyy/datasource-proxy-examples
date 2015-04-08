package net.ttddyy.dsproxy.example.jndi.lookup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author Tadaya Tsuyukubo
 */
@SpringBootApplication
@RestController
public class JndiLookupApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        // putting DataSource JNDI name to system properties
        // To centralized all config in this class, not using "application.properties" on purpose
        System.setProperty("spring.datasource.jndi-name", "java:comp/env/jdbc/myDS");

        return application.sources(JndiLookupApplication.class);
    }


    @Bean
    public CommandLineRunner init() {
        return args -> {
            // populate DB
            jdbcTemplate.execute("CREATE TABLE users(id INT, name VARCHAR(255))");
            jdbcTemplate.batchUpdate("INSERT INTO users (id, name) VALUES (?, ?)",
                    Arrays.asList(new Object[]{1, "foo"}, new Object[]{2, "bar"}, new Object[]{3, "baz"}));
        };
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping
    public ModelMap index() throws Exception {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);

        ModelMap result = new ModelMap();
        result.put("count", count);
        return result;
    }


}
