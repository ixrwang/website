/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.web;

import com.qq.connect.utils.http.HttpClient;
import com.qq.connect.utils.http.PostParameter;
import com.qq.connect.utils.http.Response;
import java.util.Map;
import junit.framework.TestSuite;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

/**
 * 对微信进行测试
 * @author IXR
 */
public class PostWeiXinTest extends TestSuite{
    
    @Test
    public void test() throws Exception{
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx7eb909445a938df1&secret=16625515439867caff6ca11660ea74b5";
        HttpClient client = new HttpClient();
        Response response = client.get(url);
        String json = response.getResponseAsString();
        ObjectMapper mapper  = new ObjectMapper();
        Map<String,String> jsonObject = mapper.readValue(json, Map.class);
        String access_token = jsonObject.get("access_token");
        client = new HttpClient();
        url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token="+access_token+"&next_openid=";
        System.out.println(url);
        response = client.post(url,new PostParameter[0]);
        json = response.getResponseAsString();
        System.out.println(json);
    }
}
