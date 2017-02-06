package net.ttddyy.dsproxy.example;

import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Arrays;

/**
 * Spring java based configuration using {@link net.ttddyy.dsproxy.support.ProxyDataSourceBuilder}.
 *
 * @author Tadaya Tsuyukubo
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public DataSource actualDataSource() {
        EmbeddedDatabaseBuilder databaseBuilder = new EmbeddedDatabaseBuilder();
        return databaseBuilder.setType(EmbeddedDatabaseType.HSQL).build();
    }

    @Bean
    @Primary
    public DataSource dataSource(DataSource actualDataSource) {
        return ProxyDataSourceBuilder
                .create(actualDataSource)
                .name("MyDS")
                .logQueryToSysOut()
                .build();
    }

    @Bean
    CommandLineRunner init(JdbcTemplate jdbcTemplate) {    // DataSourceAutoConfiguration creates jdbcTemplate
        return args -> {

            System.out.println("**********************************************************");

            jdbcTemplate.execute("CREATE TABLE users (id INT, name VARCHAR(20))");

            jdbcTemplate.batchUpdate("INSERT INTO users (id, name) VALUES (?, ?)", Arrays.asList(new Object[][]{{1, "foo"}, {2, "bar"}}));

            PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection().prepareStatement("INSERT INTO users (id, name) VALUES (?, ?)");
            preparedStatement.setString(2, "FOO");
            preparedStatement.setInt(1, 3);
            preparedStatement.addBatch();
            preparedStatement.setInt(1, 4);
            preparedStatement.setString(2, "BAR");
            preparedStatement.addBatch();
            preparedStatement.executeBatch();

            jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
            System.out.println("**********************************************************");

        };
    }

}
