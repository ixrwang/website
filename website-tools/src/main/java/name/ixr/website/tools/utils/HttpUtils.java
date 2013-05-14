/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.tools.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * HTTP工具包
 *
 * @author IXR
 */
public class HttpUtils {
    
    public static void main(String[] args) throws Exception {
        System.out.println(new String(download("http://www.baidu.com/"),"utf-8"));
    }
    
    public static byte[] download(String url) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpUtils.download(url, baos);
        return baos.toByteArray();
    }

    public static void download(String url, OutputStream stream) throws Exception {
        HttpGet get = new HttpGet(url);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        byte[] bytes = new byte[1024];
        int idx;
        while ((idx = is.read(bytes)) > 0) {
            stream.write(bytes, 0, idx);
        }
        stream.flush();
        stream.close();
    }
}
