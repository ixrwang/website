/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.app;

import java.util.UUID;
import name.ixr.website.app.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 用户信息的测试
 *
 * @author IXR
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/root-context.xml")
public class UserTest extends AbstractJUnit4SpringContextTests {
    @Autowired
    private UserService userService;
    
    @Test
    public void register() {
        User user = new User();
        user.setId("00000000-0000-0000-0000-000000000000");
        user.setAccount("ixr_wang");
        user.setPassword("123456");
        user.setNickname("来自冰星的生物");
        //assert userService.deleteByAccount(user.getAccount()) >= 0;
        assert userService.register(user) == 1;
        assert userService.deleteByAccount(user.getAccount()) >= 0;
    }
    @Test
    public void register2() {
        for (int i = 0; i < 100000; i++) {
            logger.info("----------------------- : " + i);
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setAccount(UUID.randomUUID().toString());
            user.setPassword("test");
            user.setNickname("test");
            assert userService.register(user) == 1;
        }
    }
}
