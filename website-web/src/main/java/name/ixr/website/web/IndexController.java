/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页
 *
 * @author IXR
 */
@Controller
public class IndexController {

    @RequestMapping({"/", "/index"})
        public String index(Model model) {
        return "/index";
    }
}
