/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.web.inteceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 对于SESSION的处理操作
 * @author IXR
 */
public class SessionInteceptor extends HandlerInterceptorAdapter{
    
    /** 会话保持时间，单位秒 3600 为一小时 */
    private static final int MAX_INACTIVE_INTERVAL = 3600 ;
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //避免URL里带入JSESSION问题造成问题
        if(request.getSession().isNew()) {
            request.getSession().invalidate();
            request.getSession().setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
        }
    }
    
}
