/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.web;

import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;
import javax.servlet.http.HttpServletRequest;
import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * 对QQ的测试
 * @author IXR
 */
public class QQSdkTest extends TestCase{
    
   public void testOauth() throws Exception {
       MockHttpServletRequest request = new MockHttpServletRequest();
       request.setRequestedSessionId("1111111111");
       String url = new Oauth().getAuthorizeURL(request);
       System.out.println(url);
       String code = "3F6DC7F8DC6CCFF5CBAAA66851A079FF";
       //request.setParameter("code", "3F6DC7F8DC6CCFF5CBAAA66851A079FF");
       request.setRequestURI("http://www.ixr.name/qc_back?code=E29B39EE36C3AA10F86A05A1F1E513B7&state=13818465460244c7d6fd690298420f74");
       request.setParameter("code", "E29B39EE36C3AA10F86A05A1F1E513B7");
       request.setParameter("state", "13818465460244c7d6fd690298420f74");
       System.out.println(request.getParameter("code"));
       AccessToken token = new Oauth().getAccessTokenByRequest(request);
       System.out.println(token.getAccessToken());
//       OpenID openIDObj =  new OpenID(accessToken);
//       String openID = openIDObj.getUserOpenID();
//       UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
//       System.out.println(qzoneUserInfo.getUserInfo().getNickname());
   } 
}
