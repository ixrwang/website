/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.web;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
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
import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 法律
 *
 * @author IXR
 */
@Controller
public class LegalController {

    public static List<jg> slt(String content) throws Exception {
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
                result.add(new jg(title, text, doc.get("href")));
            }
        }
        return result;
    }

    @RequestMapping({"/legal"})
    public String index(@RequestParam(defaultValue = "中华人民共和国")String search, Model model) throws Exception {
        Long startTime = System.currentTimeMillis(); 
        List<jg> result = slt(search);
        Long endTime = System.currentTimeMillis();  
        model.addAttribute("list", result);
        model.addAttribute("time", (endTime-startTime));
        model.addAttribute("search", search);
        return "/legal";
    }

    public static class jg {

        public jg() {
        }

        ;
    public jg(String title, String context, String href) {
            this.title = title;
            this.context = context;
            this.href = href;
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
