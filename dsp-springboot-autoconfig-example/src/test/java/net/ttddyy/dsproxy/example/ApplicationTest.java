package net.ttddyy.dsproxy.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void test() {
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
