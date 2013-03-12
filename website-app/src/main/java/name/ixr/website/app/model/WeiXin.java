/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.app.model;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信消息载体
 * @author IXR
 */
@XmlRootElement(name = "xml")
public class WeiXin {
    
    public static WeiXin newResponse(WeiXin request) {
        WeiXin response = new WeiXin();
        response.FromUserName = request.ToUserName;
        response.ToUserName = request.FromUserName;
        return response;
    }
    
    public String ToUserName;//开发者微信号
    public String FromUserName;//发送方帐号（一个OpenID）
    public Long CreateTime = new Date().getTime();//消息创建时间 （整型）
    public String MsgType;//消息类型，text:文本消息,image:图片消息,location:地理位置消息,链接消息:link,event:事件推送,voice:语音信息
    public String Event;//事件类型，有ENTER(进入会话)和LOCATION(地理位置)
    public String Latitude;//地理位置维度，事件类型为LOCATION的时存在
    public String Longitude;//地理位置经度，事件类型为LOCATION的时存在
    public String Precision;//地理位置精度，事件类型为LOCATION的时存在
    public String Content;//文本消息内容
    public String PicUrl;//图片链接
    public String Location_X;//地理位置维度
    public String Location_Y;//地理位置精度
    public String Scale;//地图缩放大小
    public String Label;//地理位置信息
    public String Title;//消息标题
    public String Description;//消息描述
    public String Url;//消息链接
    public String MsgId;//消息id，64位整型
    
    public String MediaId;//音频ID;//SAeRXK3u8_7K7QRBKSV2iOK4Hb8mdHRo2HGLlV_26F6sgSi-FS-ZRqh044a4tUN-
    public String Format;//格式化;//amr
    
    //以下是回复
    public Integer FuncFlag = 1;//位1被标志时，星标刚收到的消息，发送是为0。
    //回复文本消息 MsgType:text
    // ToUserName FromUserName CreateTime MsgType Content FuncFlag
    //回复音乐消息 MsgType:music
    // ToUserName FromUserName CreateTime MsgType MusicUrl HQMusicUrl FuncFlag
    //回复图文消息 MsgType:news
    // ToUserName FromUserName CreateTime MsgType ArticleCount Articles Title Description PicUrl Url
    public Integer ArticleCount;//图文消息个数，限制为10条以内
    public List<Article> Articles;//多条图文消息信息，默认第一个item为大图
    
    
    /**
     * 发送音乐的信息
     */
    @XmlRootElement(name = "Music")
    public static class Music {
        public String Title;//标题
        public String Description;//说明
        public String MusicUrl;//音乐链接
        public String HQMusicUrl;//高质量音乐链接，WIFI环境优先使用该链接播放音乐
    }
    @XmlRootElement(name = "item")
    public static class Article {
        public String Title;//图文消息标题
        public String Description;//图文消息描述
        public String PicUrl;//图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80，限制图片链接的域名需要与开发者填写的基本资料中的Url一致
        public String Url;//点击图文消息跳转链接
    }
    
}
