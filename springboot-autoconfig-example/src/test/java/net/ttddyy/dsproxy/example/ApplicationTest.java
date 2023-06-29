package net.ttddyy.dsproxy.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void test() {
       User user = new User();
       user.setName("foo");
       User user1 = new User();
       user1.setName("bar");
       
       List<User> userList = new ArrayList<>();
       userList.add(user);
       userList.add(user1);
       
       userRepository.saveAll(userList);
       
       long count = userRepository.count();
       
       assertThat(count).isEqualTo(2);
    }

}
