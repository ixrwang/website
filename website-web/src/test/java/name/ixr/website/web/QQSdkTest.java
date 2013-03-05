/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.web;

import com.qq.connect.oauth.Oauth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 对QQ的测试
 * @author IXR
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/root-context.xml","classpath:/META-INF/servlet-context.xml"})
public class QQSdkTest extends AbstractJUnit4SpringContextTests{
    
    @Test
   public void oauth() throws Exception {
       MockHttpServletRequest request = new MockHttpServletRequest();
       request.setSession(new MockHttpSession());
       String url = new Oauth().getAuthorizeURL(request);
       logger.debug(url);
   } 
}
