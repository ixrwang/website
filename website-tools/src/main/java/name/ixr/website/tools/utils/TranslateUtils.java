/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.tools.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * 翻译工具包
 * @author IXR
 */
public class TranslateUtils {
    
    public static void main(String[] args) throws Exception {
        TranslateInfo translateInfo = getTranslateInfo("北京", Language.CN, Language.EN);
        System.out.println(translateInfo.toString());
    }
    
    public static String getTTS(String context, Language language) throws Exception {
        String format = "/translate_tts?ie=UTF-8&q={0}&tl={1}";
        String url = MessageFormat.format(format, URLEncoder.encode(context, "UTF-8"), language.getLanguage());
        return url;
    }
    public static TranslateInfo getTranslateInfo(String text,Language source,Language to) {
        return getTranslateInfo(text, source, to, Language.CN);
    }
    public static TranslateInfo getTranslateInfo(String text,Language source,Language to,Language hl) {
        try {
            String ip = InetAddress.getByName("translate.google.com").getHostAddress();
            String format = "http://{0}/translate_a/t?client=m&text={1}&sl={2}&tl={3}&ie=UTF-8&oe=UTF-8&hl=" + hl.language;
            String url = MessageFormat.format(format, ip, URLEncoder.encode(text, "UTF-8"),source.getLanguage(),to.getLanguage());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            HttpUtils.download(url, baos);
            String json = new String(baos.toByteArray(), "UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            TranslateInfo translateInfo = objectMapper.readValue(json, TranslateInfo.class);
            TranslateInfo.Status status = new TranslateInfo.Status();
            status.setCode("200");
            translateInfo.setStatus(status);
            return translateInfo;
        } catch (Exception ex) {
            TranslateInfo error = new TranslateInfo();
            TranslateInfo.Status status = new TranslateInfo.Status();
            status.setCode("500");
            status.setRequest(ex.getMessage());
            error.setStatus(status);
            return error;
        }
    }
    
    public static class Language {
        private String language;
        private String name;
        private static Map<String,Language> languages = new HashMap<>();
        private static List<Language> languageList = new ArrayList<>();

        @Override
        public String toString() {
            return language;
        }
        
        public static Language newLanguage(String name,String language) {
            Language l = toLanguage(language);
            if(l == null) {
                l = new Language(name, language);
            }
            return l;
        }
        
        public static void removeLanguage(Language language) {
            languages.remove(language.getLanguage());
            languageList.remove(language);
        }
        
        public static void clearLanguage() {
            languages.clear();
            languageList.clear();
        }
        
        private Language(String name,String language) {
            this.name = name;
            this.language = language;
            init();
        }
        
        private void init() {
            languages.put(language, this);
            languageList.add(this);
        }

        public String getName() {
            return name;
        }
        
        public String getLanguage() {
            return language;
        }
        
        public static Language[] getLanguages() {
            return languageList.toArray(new Language[0]);
        }
        
        public static Language toLanguage (String language) {
            return languages.get(language);
        }
        /**检测语言*/
        public final static Language AUTO = new Language("检测语言","auto");
        /**中文*/
        public final static Language CN = new Language("中文","zh-CN");
        /**英文*/
        public final static Language EN = new Language("英文","en");
        /**日语*/
        public final static Language JA = new Language("日语","ja");
        /**韩语*/
        public final static Language KO = new Language("韩语","ko");
        /**繁体*/
        public final static Language TW = new Language("繁体","zh-TW");
        /**法语*/
        public final static Language RU = new Language("法语","ru");
        /**德语*/
        public final static Language DE = new Language("德语","de");
        /**俄语*/
        public final static Language FR = new Language("俄语","fr");
        
    }
    
    public static class TranslateInfo {
        private Status status;
        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }
        public static class Status {
            private String code;
            public String getCode() {
                return code;
            }
            public void setCode(String code) {
                this.code = code;
            }
            private String request;
            public String getRequest() {
                return request;
            }
            public void setRequest(String request) {
                this.request = request;
            }
        }
        private List<Sentences> sentences;
        public List<Sentences> getSentences() {
            return sentences;
        }
        public void setSentences(List<Sentences> sentences) {
            this.sentences = sentences;
        }
        public static class Sentences {
            private String trans;
            public String getTrans() {
                return trans;
            }
            public void setTrans(String trans) {
                this.trans = trans;
            }
            private String orig;
            public String getOrig() {
                return orig;
            }
            public void setOrig(String orig) {
                this.orig = orig;
            }
            private String translit;
            public String getTranslit() {
                return translit;
            }
            public void setTranslit(String translit) {
                this.translit = translit;
            }
            private String src_translit;
            public String getSrc_translit() {
                return src_translit;
            }
            public void setSrc_translit(String src_translit) {
                this.src_translit = src_translit;
            }
        }
        private List<Dict> dict;
        public List<Dict> getDict() {
            return dict;
        }
        public void setDict(List<Dict> dict) {
            this.dict = dict;
        }
        public static class Dict {
            private String pos;
            public String getPos() {
                return pos;
            }
            public void setPos(String pos) {
                this.pos = pos;
            }
            private List<String> terms;
            public List<String> getTerms() {
                return terms;
            }
            public void setTerms(List<String> terms) {
                this.terms = terms;
            }
            private List<Entry> entry;
            public List<Entry> getEntry() {
                return entry;
            }
            public void setEntry(List<Entry> entry) {
                this.entry = entry;
            }
            public static class Entry {
                private String word;
                public String getWord() {
                    return word;
                }
                public void setWord(String word) {
                    this.word = word;
                }
                private List<String> reverse_translation;
                public List<String> getReverse_translation() {
                    return reverse_translation;
                }
                public void setReverse_translation(List<String> reverse_translation) {
                    this.reverse_translation = reverse_translation;
                }
                private String score;
                public String getScore() {
                    return score;
                }
                public void setScore(String score) {
                    this.score = score;
                }
            }
        }
        private String src;
        public String getSrc() {
            return src;
        }
        public void setSrc(String src) {
            this.src = src;
        }
        private String server_time;
        public String getServer_time() {
            return server_time;
        }
        public void setServer_time(String server_time) {
            this.server_time = server_time;
        }

        @Override
        public String toString() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            try {
                return objectMapper.writeValueAsString(this);
            } catch (IOException ex) {
                throw new Error(ex);
            }
        }
    }

}
