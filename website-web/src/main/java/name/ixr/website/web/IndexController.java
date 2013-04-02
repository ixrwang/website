/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.web;

import java.util.List;
import name.ixr.website.app.ArticleService;
import name.ixr.website.app.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 首页
 *
 * @author IXR
 */
@Controller
public class IndexController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping({"/", "/index"})
    public String index(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size, String search, Model model) {
        long count = articleService.queryCount(search);
        if (count > 0) {
            model.addAttribute("articles", articleService.queryByPage(page, size, search));
        }
        return "/index";
    }
}
