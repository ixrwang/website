/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.web;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 开放接口
 * @author IXR
 */
@Controller
public class OAuthController {
    
    @RequestMapping({"/oauth/qq_login"})
    public String qq_login(HttpServletRequest request) throws QQConnectException {
        return "redirect:" + new Oauth().getAuthorizeURL(request);
    }
    
    @RequestMapping({"/oauth/qq_login_back"})
    public String qq_login_back(Model model,HttpServletRequest request) throws QQConnectException {
        AccessToken accessTokenObj = new Oauth().getAccessTokenByRequest(request);
        if (accessTokenObj.getAccessToken().equals("")) {
            throw new QQConnectException("未经授权的访问");
        } else {
            String accessToken = accessTokenObj.getAccessToken();
            // 利用获取到的accessToken 去获取当前用的openid -------- start
            OpenID openIDObj =  new OpenID(accessToken);
            UserInfo qzoneUserInfo = new UserInfo(accessToken, openIDObj.getUserOpenID());
            model.addAttribute("openIDObj",openIDObj);
            model.addAttribute("qzoneUserInfo",qzoneUserInfo.getUserInfo());
        }
        return "/oauth/qq_login_back";
    }
}
