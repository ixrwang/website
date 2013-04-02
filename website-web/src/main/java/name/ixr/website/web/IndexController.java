/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.web;

import java.util.ArrayList;
import name.ixr.website.app.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
        int count = articleService.queryCount(search);
        int total = count / size + (count % size > 0 ? 1 : 0);
        page = page < 1 ? 1 : page > total ? total : page;
        if (count > 0) {
            model.addAttribute("articles", articleService.queryByPage(page, size, search));
        } else {
            model.addAttribute("articles", new ArrayList<>());
        }
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("count", count);
        model.addAttribute("total", total);
        model.addAttribute("search", search);
        return "/index";
    }
}
