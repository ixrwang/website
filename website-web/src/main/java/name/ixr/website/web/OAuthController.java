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
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import name.ixr.website.app.OAuthService;
import name.ixr.website.app.UserService;
import name.ixr.website.app.model.OAuth;
import name.ixr.website.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 开放接口
 * @author IXR
 */
@Controller
public class OAuthController {
    
    
    
    @Autowired
    private OAuthService oauthService;
    
    @Autowired
    private UserService userService;
    
    @RequestMapping({"/oauth/qq_login"})
    public String qq_login(HttpServletRequest request) throws QQConnectException {
        String url = new Oauth().getAuthorizeURL(request);//
        //处理JESSIONID问题
        url = url.replace(";jsessionid=" + request.getSession().getId(), "");
        return "redirect:" + url;
    }
    
    @RequestMapping({"/oauth/qq_login_back"})
    public String qq_login_back(HttpServletRequest request) throws QQConnectException {
        AccessToken accessTokenObj = new Oauth().getAccessTokenByRequest(request);
        if (accessTokenObj.getAccessToken().equals("")) {
            throw new QQConnectException("未经授权的访问");
        } else {
            String accessToken = accessTokenObj.getAccessToken();
            // 利用获取到的accessToken 去获取当前用的openid -------- start
            OpenID openIDObj =  new OpenID(accessToken);
            UserInfo qzoneUserInfo = new UserInfo(accessToken, openIDObj.getUserOpenID());
            User user = oauthService.login(openIDObj.getUserOpenID(), OAuthService.QQ_LOGIN_TYPE);
            // 如果他没有用户关联，就注册一个随机账号
            if(user == null) {
                user = new User();
                user.setId(UUID.randomUUID().toString());
                user.setAccount(UUID.randomUUID().toString());
                user.setPassword(UUID.randomUUID().toString());
                user.setNickname(qzoneUserInfo.getUserInfo().getNickname());
                userService.register(user);
                OAuth oAuth = new OAuth();
                oAuth.setRid(user.getId());
                oAuth.setType(OAuthService.QQ_LOGIN_TYPE);
                oAuth.setOid(openIDObj.getUserOpenID());
                oauthService.save(oAuth);
            }
            request.getSession().setAttribute(LoginController.USER_LOGIN, user);
            return "redirect:/";
        }
    }
}
