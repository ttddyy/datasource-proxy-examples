package net.ttddyy.dsproxy.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Spring XML based configuration.
 *
 * @author Tadaya Tsuyukubo
 */
@SpringBootApplication
@ImportResource("classpath:applicationContext.xml")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    CommandLineRunner init(DataSource dataSource,
            //    CommandLineRunner init(ProxyConfig proxyConfig, DataSource dataSource,
            JdbcTemplate jdbcTemplate) {
        System.out.println(dataSource);
        return args -> {
            System.out.println("**********************************************************");
            //            System.out.println(proxyConfig.getDataSourceName());
            jdbcTemplate.execute("CREATE TABLE users (id INT, name VARCHAR(20))");
            System.out.println("**********************************************************");
            //
        };
    }
    //    @Bean
    //    CommandLineRunner init(
    //            JdbcTemplate jdbcTemplate, DataSource ds) {    // DataSourceAutoConfiguration creates jdbcTemplate
    //        return args -> {
    //
    //            System.out.println("**********************************************************");
    //
    //            jdbcTemplate.execute("CREATE TABLE users (id INT, name VARCHAR(20))");
    //
    //            jdbcTemplate.batchUpdate("INSERT INTO users (id, name) VALUES (?, ?)",
    //                    Arrays.asList(new Object[][] { { 1, "foo" }, { 2, "bar" } }));
    //
    //            PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection()
    //                    .prepareStatement("INSERT INTO users (id, name) VALUES (?, ?)");
    //            preparedStatement.setString(2, "FOO");
    //            preparedStatement.setInt(1, 3);
    //            preparedStatement.addBatch();
    //            preparedStatement.setInt(1, 4);
    //            preparedStatement.setString(2, "BAR");
    //            preparedStatement.addBatch();
    //            preparedStatement.executeBatch();
    //
    //            jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
    //            System.out.println("**********************************************************");
    //
    //            Connection conn = ds.getConnection();
    //            PreparedStatement ps = conn.prepareStatement("CREATE TABLE users (id INT, name VARCHAR(20))");
    //            ps.setString(100, "AAA");
    //
    //        };
    //    }

}
