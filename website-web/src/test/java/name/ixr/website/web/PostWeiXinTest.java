/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.web;

import com.qq.connect.utils.http.HttpClient;
import com.qq.connect.utils.http.PostParameter;
import com.qq.connect.utils.http.Response;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestSuite;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

/**
 * 对微信进行测试
 * @author IXR
 */
public class PostWeiXinTest extends TestSuite{
    
    @Test
    public void test() throws Exception{
        String url = "http://mp.weixin.qq.com/cgi-bin/singlesend?t=ajax-response";
        HttpClient client = new HttpClient();
        List<PostParameter> params = new ArrayList<>();
        params.add(new PostParameter("type", "1"));
        params.add(new PostParameter("content", "test"));
        params.add(new PostParameter("tofakeid", "1053553840"));
        PostMethod post = new PostMethod(url);
        StringBuilder cookies = new StringBuilder();
        cookies.append("cert=m1sXLrNjPm7T_do1V76E8bmIT7XRtiFN;");
        cookies.append("slave_user=gh_2aa2ba4572d1;");
        cookies.append("slave_sid=bTFzWExyTmpQbTdUX2RvMVY3NkU4Ym1JVDdYUnRpRk5pNkgydnRsa05xYU1DeE13RVNhTjJ1c1dCb0sxRW9NV3N0WFhXZmdKR3BzaFdkTHo1VGo1bUpaVzZHVkozSEV0OXlxMk9FSnQwYklXbXF0cmtyTmpDdEo1Yld1ampqZXo;");
        post.addRequestHeader("Cookie", cookies.toString());
        post.addParameter("type", "1");
        post.addParameter("content", "test");
        post.addParameter("tofakeid", "1053553840");
        Response response = client.httpRequest(post);
        System.out.println(response.getResponseAsString());
    }
}
