/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.web;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import junit.framework.TestSuite;
import name.ixr.website.app.model.WeiXin;
import org.junit.Test;

/**
 *
 * @author IXR
 */
public class XmlTest extends TestSuite{
    
    @Test
    public void test() throws Exception{
        JAXBContext context = JAXBContext.newInstance(WeiXin.class);
        Marshaller marshaller = context.createMarshaller();
        StringWriter xml = new StringWriter();
        WeiXin request = new WeiXin();
        request.Content = "123";
        marshaller.marshal(request,xml);
        System.out.println(xml.toString());
    }
}
