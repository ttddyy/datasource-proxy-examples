package net.ttddyy.dsproxy.example.mybatis;

import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;
import net.ttddyy.dsproxy.listener.logging.SystemOutQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(String[] args) throws SQLException {
        Environment environment = getEnvironment();
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(UserMapper.class);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);

            Usertable newUser = new Usertable();
            newUser.setId(3);
            newUser.setName("Charlie");
            mapper.insert(newUser);
            session.commit();
//
//            List<Usertable> users = mapper.findAll();
//            for (Usertable u : users) {
//                System.out.println(u);
//            }
        }
    }

    private static Environment getEnvironment() throws SQLException {
        DataSource dataSource = getDataSource();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(255))");
            stmt.execute("INSERT INTO users (id, name) VALUES (1, 'Alice'), (2, 'Bob')");
        }
        return new Environment("dev", new JdbcTransactionFactory(), dataSource);
    }

    static class Usertable {
        private int id;
        private String name;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Usertable{id=" + id + ", name='" + name + "'}";
        }
    }

    interface UserMapper {
        @Select("SELECT id, name FROM users")
        List<Usertable> findAll();

        @Insert("INSERT INTO users (id, name) VALUES (#{id}, #{name})")
        void insert(Usertable user);
    }

    private static DataSource getDataSource() {
        SystemOutQueryLoggingListener listener = new SystemOutQueryLoggingListener();
        listener.setQueryLogEntryCreator(new DefaultQueryLogEntryCreator());

        return ProxyDataSourceBuilder.create(getH2DataSource())
                .name("ProxyDataSource")
                .listener(listener)
                .countQuery()
                .multiline()
                .logSlowQueryToSysOut(1, TimeUnit.MINUTES)
                .build();
    }

    private static DataSource getH2DataSource() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("");
        return ds;
    }
}
