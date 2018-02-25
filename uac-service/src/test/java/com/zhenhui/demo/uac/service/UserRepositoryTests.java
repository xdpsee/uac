package com.zhenhui.demo.uac.service;

import com.zhenhui.demo.uac.core.dataobject.User;
import com.zhenhui.demo.uac.core.repository.UserRepository;
import com.zhenhui.demo.uac.core.repository.exception.UserAlreadyExistsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserQuery() {

        User user = userRepository.queryUser(1L);
        assertNotNull(user);

        user = userRepository.queryUser("18621816233");
        assertNotNull(user);

    }

    @Test
    public void testUserUpdate() {

        User user = userRepository.queryUser("18621816233");
        assertNotNull(user);

        userRepository.updateAvatar(user.getId(), "http://assets-glassx.qiniudn.com/uploads/user/avatar/147/blue.png");
        userRepository.updateSecret(user.getId(), "Abc123456");

        user = userRepository.queryUser(1L);
        assertEquals("http://assets-glassx.qiniudn.com/uploads/user/avatar/147/blue.png", user.getAvatar());
        assertEquals("Abc123456", user.getSecret());

        user = userRepository.queryUser("18621816233");
        assertEquals("http://assets-glassx.qiniudn.com/uploads/user/avatar/147/blue.png", user.getAvatar());
        assertEquals("Abc123456", user.getSecret());

    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testUserCreateException() throws Exception {

        userRepository.createUser("18621816233", "123456");

    }

    @Test
    public void testUserCreate() throws Exception {
        User user = userRepository.createUser("13402022080", "123456");
        assertNotNull(user.getId());
    }

}
