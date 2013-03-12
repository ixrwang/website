/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.app;

import java.util.UUID;
import name.ixr.website.app.model.OAuth;
import name.ixr.website.app.model.User;
import name.ixr.website.app.model.WeiXin;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 微信的业务逻辑
 *
 * @author IXR
 */
@Service
public class WeiXinService {
    
    private final Log logger = LogFactory.getLog(WeiXinService.class);

    @Autowired
    private OAuthService oauthService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 用来处理微信业务
     *
     * @param request 信息包
     * @return
     */
    public WeiXin handle(WeiXin request) {
        WeiXin response = null ;
        switch (request.MsgType) {
            case "text":
                // 用户关注
                if("Hello2BizUser".equals(request.Content)) {
                    response = Hello2BizUser(request);
                } else {
                    response = Chat(request);
                }
                break;
            case "event":
                // 用户取消关注
                if("unsubscribe".equals(request.Event)) {
                    response = unsubscribe(request);
                }
                break;
            default:
                break;
        }
        logger(request,response);
        return response;
    }
    /**
     * 对内容信息的处理
     * @param request
     * @return 
     */
    private WeiXin Chat(WeiXin request) {
        WeiXin response = WeiXin.newResponse(request);
        response.MsgType = "text";
        response.Content = request.Content.replaceAll("你", "你也") + "[微笑]";
        return response;
    }
    /**
     * 微信关注
     * @param request
     * @return 
     */
    private WeiXin Hello2BizUser(WeiXin request) {
        WeiXin response = WeiXin.newResponse(request);
        
        // 注册新用户进行绑定
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setAccount(UUID.randomUUID().toString());
        user.setPassword(UUID.randomUUID().toString());
        user.setNickname(UUID.randomUUID().toString());
        userService.register(user);
        OAuth oAuth = new OAuth();
        oAuth.setRid(user.getId());
        oAuth.setType(OAuthService.WEIXIN_OPENID);
        oAuth.setOid(request.FromUserName);
        oauthService.save(oAuth);
        
        response.MsgType = "text";
        response.Content = "感谢您的关注";
        return response;
    }
    
    /**
     * 取消关注
     * @param request
     * @return 
     */
    @Transactional
    private WeiXin unsubscribe(WeiXin request) {
        User user = oauthService.login(request.FromUserName, OAuthService.WEIXIN_OPENID);
        if (user != null) {
            userService.deleteById(user.getId());
        }
        oauthService.deleteByOidAndType(request.FromUserName, OAuthService.WEIXIN_OPENID);
        return null;
    }
    
    private void logger(WeiXin request,WeiXin response) {
        if(request != null || response != null ) {
            StringBuilder msg = new StringBuilder();
            if(request != null) {
                msg.append("\r --------------------------- Request --------------------------");
                logger(msg,request);
            }
            if(response != null) {
                msg.append("\r --------------------------- Response -------------------------");
                logger(msg,response);
            }
            msg.append("\r --------------------------- End ------------------------------");
            logger.debug(msg);
        }
    }
    private void logger(StringBuilder msg,WeiXin wx) {
        msg.append("\r FromUserName : ").append(wx.FromUserName);
        msg.append("\r ToUserName : ").append(wx.ToUserName);
        if("event".equals(wx.MsgType)) {
            msg.append("\r Event : ").append(wx.Event);
        } else {
            msg.append("\r MsgType : ").append(wx.MsgType);
            msg.append("\r Content : ").append(wx.Content);
        }
    }
}
