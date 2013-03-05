/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.web;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户登录
 * @author IXR
 */
@Controller
public class LoginController {
    /** 用户在SESSION里的KEY */
    public static final String USER_LOGIN = "USER_LOGIN";
    
    @RequestMapping({"/logout"})
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }
}
