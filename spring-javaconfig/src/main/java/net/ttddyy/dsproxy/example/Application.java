package net.ttddyy.dsproxy.example;

import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Arrays;

/**
 * @author Tadaya Tsuyukubo
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public DataSource actualDataSource() {
        DataSourceBuilder factory = DataSourceBuilder
                .create(Application.class.getClassLoader())
                .driverClassName("org.hsqldb.jdbcDriver")
                .url("jdbc:hsqldb:mem:testdb")
                .username("sa")
                .password("");
        return factory.build();
    }

    @Bean
    public DataSource dataSource(DataSource actualDataSource) {
//        ProxyDataSource dataSource = new ProxyDataSource();
//        dataSource.setDataSource(originalDataSource);
//        dataSource.setListener(new SystemOutQueryLoggingListener());
//        return dataSource;

        return ProxyDataSourceBuilder
                .create(actualDataSource)
                .logQueryToSysOut()
                .build();
    }

    @Bean
    CommandLineRunner init(JdbcTemplate jdbcTemplate) {
        return args -> {
            jdbcTemplate.execute("CREATE TABLE users (id INT, name VARCHAR(20))");

//            jdbcTemplate.batchUpdate("INSERT INTO users (id, name) VALUES (?, ?)", Arrays.asList(new Object[][]{{1, "foo"}, {2, "bar"}}));

            PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection().prepareStatement("INSERT INTO users (id, name) VALUES (?, ?)");
            preparedStatement.setString(2, "FOO");
            preparedStatement.setInt(1, 3);
            preparedStatement.addBatch();
            preparedStatement.setInt(1, 4);
            preparedStatement.setString(2, "BAR");
            preparedStatement.addBatch();
            preparedStatement.executeBatch();


        };
    }

}
