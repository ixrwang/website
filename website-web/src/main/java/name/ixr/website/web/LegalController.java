/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.web;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.ExpectationMaximizationSVDFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.Factorization;
import org.apache.mahout.cf.taste.impl.recommender.svd.Factorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.PersistenceStrategy;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 法律
 *
 * @author IXR
 */
@Controller
public class LegalController implements InitializingBean {

    @Autowired
    private DataSource source;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private long maxid = 0;

    @Override
    public void afterPropertiesSet() throws Exception {
        maxid = jdbcTemplate.queryForLong("SELECT MAX(user_id) FROM taste_preferences");
    }
    
    @ResponseBody
    @RequestMapping({"/legal/similarity"})
    public String similarity(@CookieValue long user_id, long item_id, HttpServletResponse response) throws Exception {
        List<Double> list = jdbcTemplate.queryForList("SELECT preference FROM taste_preferences WHERE user_id=? AND item_id=?", new Object[]{user_id, item_id}, Double.class);
        if (list.size() > 0) {
            double preference = list.get(0);
            if (preference < 5) {
                preference++;
                jdbcTemplate.update("UPDATE taste_preferences SET preference=? WHERE user_id=? AND item_id=?", new Object[]{preference, user_id, item_id});
            }
        } else {
            jdbcTemplate.update("INSERT INTO taste_preferences VALUES(?,?,?)", new Object[]{user_id, item_id, 1});
        }
        return "SUCCESS";
    }

    public static void add(String title, String content, String href) throws Exception {
        Directory dir = FSDirectory.open(new File("/opt/lucenedata"));
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
        try (IndexWriter writer = new IndexWriter(dir, config)) {
            Document doc = new Document();
            doc.add(new Field("title", title, Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("content", content, Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("href", href, Field.Store.YES, Field.Index.ANALYZED));
            writer.addDocument(doc);
        }
    }

    public List<jg> slt(String content) throws Exception {
        List<jg> result = new LinkedList<>();
        Directory dir = FSDirectory.open(new File("/opt/lucenedata"));
        try (IndexReader reader = IndexReader.open(dir); IndexSearcher search = new IndexSearcher(reader)) {
            QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
            Query query = parser.parse(content);
            TopDocs tdocs = search.search(query, 10);
            ScoreDoc[] sdocs = tdocs.scoreDocs;
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));
            // 这个100是指定关键字字符串的context的长度，你可以自己设定，因为不可能返回整篇正文内容 
            highlighter.setTextFragmenter(new SimpleFragmenter(100));
            for (ScoreDoc sdoc : sdocs) {
                Document doc = search.doc(sdoc.doc);
                String text = doc.get("content");
                String title = doc.get("title");
                org.jsoup.nodes.Document document = Jsoup.parse(text);
                text = highlighter.getBestFragment(new StandardAnalyzer(Version.LUCENE_35), content, document.text());
                title = highlighter.getBestFragment(new StandardAnalyzer(Version.LUCENE_35), content, title);
                if (title == null) {
                    title = doc.get("title");
                }
                if (text == null) {
                    text = doc.get("content");
                    if (text.length() > 100) {
                        text = text.substring(0, 100);
                    }
                }
                result.add(new jg(sdoc.doc, title, text, doc.get("href")));
            }
        }
        return result;
    }

    @RequestMapping({"/legal"})
    public String index(@CookieValue(defaultValue = "0") long user_id,@RequestParam(defaultValue = "中华人民共和国") String search, HttpServletResponse response,Model model) throws Exception {
        if (user_id == 0) {
            user_id = ++maxid;
            Cookie cookie = new Cookie("user_id", Long.toString(user_id));
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);
        }
        Long startTime = System.currentTimeMillis();
        List<jg> result = slt(search);
        Long endTime = System.currentTimeMillis();
        model.addAttribute("list", result);
        model.addAttribute("time", (endTime - startTime));
        model.addAttribute("search", search);
        
        //推荐
        MySQLJDBCDataModel msqljdbcdm = new MySQLJDBCDataModel(source);//加载数据模型，供机器学习使用
        Factorizer factorizer = new ExpectationMaximizationSVDFactorizer(msqljdbcdm, 20, 50);
        // 获取SVD分解结果（用户特征矩阵和产品特征矩阵）
        final Factorization factorization = factorizer.factorize();
        // 创建推荐引擎
        SVDRecommender recommander = new SVDRecommender(msqljdbcdm, factorizer, new PersistenceStrategy() {
                @Override
                public void maybePersist(Factorization factorization) throws IOException {
                        throw new IOException("not rewritable!");
                }
                @Override
                public Factorization load() throws IOException {
                        return factorization;
                }
        });
        List<jg> recommendeds = new LinkedList<>();
        try{
            List<RecommendedItem> recommendations = recommander.recommend(user_id, 3); //给ID为1的顾客推荐3个产品
            Directory dir = FSDirectory.open(new File("/opt/lucenedata"));
            try (IndexReader reader = IndexReader.open(dir)) {
                for (int i = 0; i < recommendations.size(); i++) {
                    RecommendedItem recommendedItem = recommendations.get(i);
                    Document doc = reader.document((int)recommendedItem.getItemID());
                    String text = doc.get("content");
                    String title = doc.get("title");
                    recommendeds.add(new jg(recommendedItem.getItemID(), title, text, doc.get("href")));
                }
            }
        }catch (Exception ex) {
            
        }
        model.addAttribute("recommendeds", recommendeds);
        model.addAttribute("user_id", user_id);
        return "/legal";
    }
    
    
    @ResponseBody
    @RequestMapping({"/legal/idx"})
    public String idx() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list();
                } catch (Exception ex) {
                }
            }
        }).start();
        return "SUCCESS";
    }

    public void list() throws Exception {
        new File("/opt/lucenedata").deleteOnExit();
        org.jsoup.nodes.Document document = Jsoup.connect("http://www.jincao.com/t1.htm").get();
        Element element = document.select("table[width=440]").first();
        Elements links = element.select("a");
        for (Element link : links) {
            String href = "http://www.jincao.com/" + link.attr("href");
            String name = link.text().replace(" ", "");
            System.out.println(href + "\t" + name);
            org.jsoup.nodes.Document document2 = Jsoup.connect(href).get();
            Element element2 = document2.select("table[width=700]").first();
            Elements links2 = element2.select("a[href]");
            for (Element link2 : links2) {
                String href2 = href.substring(0, href.lastIndexOf("/") + 1) + link2.attr("href");
                String name2 = link2.text().replace(" ", "");
                System.out.println(href2 + "\t" + name2);
                try {
                    org.jsoup.nodes.Document document3 = Jsoup.connect(href2).get();
                    add(name2, document3.html(), href2);
                } catch (Exception ex) {
                }
            }
        }
    }

    public static class jg {

        public jg() {
        }

        public jg(long id, String title, String context, String href) {
            this.id = id;
            this.title = title;
            this.context = context;
            this.href = href;
        }
        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
        private String title;
        private String context;
        private String href;

        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title the title to set
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * @return the context
         */
        public String getContext() {
            return context;
        }

        /**
         * @param context the context to set
         */
        public void setContext(String context) {
            this.context = context;
        }

        /**
         * @return the href
         */
        public String getHref() {
            return href;
        }

        /**
         * @param href the href to set
         */
        public void setHref(String href) {
            this.href = href;
        }
    }
}
