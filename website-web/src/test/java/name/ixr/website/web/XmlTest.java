/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.web;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import junit.framework.TestSuite;
import name.ixr.website.web.model.WeiXinInfo;
import org.junit.Test;

/**
 *
 * @author IXR
 */
public class XmlTest extends TestSuite{
    
    @Test
    public void test() throws Exception{
        JAXBContext context = JAXBContext.newInstance(WeiXinInfo.class);
        Marshaller marshaller = context.createMarshaller();
        StringWriter xml = new StringWriter();
        WeiXinInfo request = new WeiXinInfo();
        request.Content = "123";
        marshaller.marshal(request,xml);
        System.out.println(xml.toString());
    }
}
