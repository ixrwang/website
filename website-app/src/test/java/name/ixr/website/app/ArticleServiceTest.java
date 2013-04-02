/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.app;

import java.util.Date;
import name.ixr.website.app.model.Article;
import name.ixr.website.tools.DataFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author IXR
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/root-context.xml")
public class ArticleServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
    
    @Autowired
    private ArticleService articleService;
    
    @Test
    @Rollback(false)
    public void add() throws Exception {
        for (int i = 0; i < 10; i++) {
            Article article = new Article();
            article.setId(DataFormat.newPrimaryKey());
            article.setTitle("测试");
            article.setContext("测试");
            article.setSummary("测试");
            article.setCreated(new Date());
            articleService.save(article);
        }
        
    }
}
