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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 法律
 *
 * @author IXR
 */
@Controller
public class LegalController {
    
    
    @ResponseBody
    @RequestMapping({"/legal/idx"})
    public String idx() throws Exception{
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
    
    public static void list() throws Exception {
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
    public String index(@RequestParam(defaultValue = "中华人民共和国") String search, Model model) throws Exception {
        Long startTime = System.currentTimeMillis();
        List<jg> result = slt(search);
        Long endTime = System.currentTimeMillis();
        model.addAttribute("list", result);
        model.addAttribute("time", (endTime - startTime));
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
