/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 微信
 * @author IXR
 */
@Controller
public class WeiXinController {
    
    @ResponseBody
    @RequestMapping(value = {"/wxbj.xml"})
    public String wxbj() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Soft>\n" +
                "<ver>5.9.3</ver>\n" +
                "<downUrl>http://pan.baidu.com/share/link?shareid=398788&uk=3794122823</downUrl>\n" +
                "<content>\n" +
                "后台数据有丢失，如果登陆不上请联系相关人,对个别人表示歉意\n" +
                "</content>\n" +
                "<softDownLoad>http://pan.baidu.com/share/link?shareid=285786&uk=3794122823</softDownLoad>\n" +
                "<tutorial>http://pan.baidu.com/share/link?shareid=210112&uk=3794122823</tutorial>\n" +
                " <welcome>请不要使用本软件发布非法讯息</welcome>\n" +
                "</Soft>";
    }
    @ResponseBody
    @RequestMapping(value = {"/soft_login.php"})
    public String soft_login(String u,String p,String cpu,String key) {
        //{"uid":"3639","user_name":"seanli","password":"d6c03dd3e4a211c54e3115f8bb36275d","QQ":"\u4f60\u597d\u554a|\u5f88\u9ad8\u5174\u8ba4\u8bc6\u4f60","type":"3","register_data":"2013-05-30 17:26:26","cpu":"1652185501","deadline":"2014-05-29 00:00:00","name_type":"VIP\u7528\u6237","key":"20b6fb40","cur_time":"2013-06-01 01:29:40"}
        return "{\"uid\":\"1\",\"user_name\":\""+u+"\",\"password\":\"c4ca4238a0b923820dcc509a6f75849b\",\"QQ\":\"1162306363\",\"type\":\"3\",\"register_data\":\"2010-01-01 00:00:00\",\"cpu\":\"1652185501\",\"deadline\":\"2020-01-01 00:00:00\",\"name_type\":\"VIP\\u7528\\u6237\",\"key\":\""+key+"\",\"cur_time\":\"2010-01-01 00:00:00\"}";
    }
}
