/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.app;

import java.util.List;
import name.ixr.website.app.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 文章信息
 *
 * @author IXR
 */
@Service
public class ArticleService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Article> queryByPage(int page, int size, String search) {
        String sql = "SELECT id,title,summary,img,created FROM t_article WHERE context like ? limit ?,?";
        int start = (page - 1) * size;
        int end = size;
        Object[] param = new Object[]{search != null ? ("%" + search + "%") : "%", start, end};
        return jdbcTemplate.query(sql, param, new BeanPropertyRowMapper<>(Article.class));
    }

    public int queryCount(String search) {
        String sql = "SELECT COUNT(*) FROM t_article WHERE context like ?";
        Object[] param = new Object[]{search != null ? ("%" + search + "%") : "%"};
        return jdbcTemplate.queryForInt(sql, param);
    }

    public int save(Article article) {
        String sql = "INSERT INTO t_article(id,title,context,summary,img,created) VALUES (?,?,?,?,?,?)";
        Object[] param = new Object[]{article.getId(), article.getTitle(), article.getContext(), article.getSummary(), article.getImg(), article.getCreated()};
        return jdbcTemplate.update(sql, param);
    }
}
