/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.app;

import name.ixr.website.app.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 文章信息
 * @author IXR
 */
@Service
public class ArticleService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public int save(Article article) {
        String sql = "INSERT INTO t_article(id,uid,title,context,created) VALUES (?,?,?,?,?)";
        Object[] param = new Object[]{article.getId(),article.getUid(),article.getTitle(),article.getContext(),article.getCreated()};
        return jdbcTemplate.update(sql, param);
    }
}
