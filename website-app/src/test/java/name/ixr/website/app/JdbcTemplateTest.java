/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.app;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试数据连接
 * @author IXR
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/root-context.xml")
public class JdbcTemplateTest extends AbstractJUnit4SpringContextTests{
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Test
    public void jdbcTemplate() {
        logger.debug(jdbcTemplate.update("show tables"));
    }
}
