/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.web;

import com.qq.connect.oauth.Oauth;
import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

/**
 * 对QQ的测试
 * @author IXR
 */
public class QQSdkTest extends TestCase{
    
   public void testOauth() throws Exception {
       MockHttpServletRequest request = new MockHttpServletRequest();
       request.setSession(new MockHttpSession());
       String url = new Oauth().getAuthorizeURL(request);
       System.out.println(url);
   } 
}
