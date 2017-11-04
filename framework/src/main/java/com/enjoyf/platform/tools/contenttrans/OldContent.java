/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.tools.contenttrans;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-3 下午2:31
 * Description:
 */
public class OldContent {
    private int blogid;

    private String uno;
    private String thumbimglink;
    private String urllink;
    private String blogsubject;
    private String content;
    private Date publishdate;
    private String blogrange;
    private String tag;

    private int feedbacknum;
    private Integer reblognum;
    private int lovenum;

    private String blogtype;
    private String isreblog;

    private String orginuno;
    private Integer orginblogid;
    private Integer contentnum;

    ///////////////////////////////////////////
    public OldContent() {
    }

    public int getBlogid() {
        return blogid;
    }

    public void setBlogid(int blogid) {
        this.blogid = blogid;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getThumbimglink() {
        return thumbimglink;
    }

    public void setThumbimglink(String thumbimglink) {
        this.thumbimglink = thumbimglink;
    }

    public String getUrllink() {
        return urllink;
    }

    public void setUrllink(String urllink) {
        this.urllink = urllink;
    }

    public String getBlogsubject() {
        return blogsubject;
    }

    public void setBlogsubject(String blogsubject) {
        this.blogsubject = blogsubject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(Date publishdate) {
        this.publishdate = publishdate;
    }

    public String getBlogrange() {
        return blogrange;
    }

    public void setBlogrange(String blogrange) {
        this.blogrange = blogrange;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getFeedbacknum() {
        return feedbacknum;
    }

    public void setFeedbacknum(int feedbacknum) {
        this.feedbacknum = feedbacknum;
    }

    public Integer getReblognum() {
        return reblognum;
    }

    public void setReblognum(Integer reblognum) {
        this.reblognum = reblognum;
    }

    public int getLovenum() {
        return lovenum;
    }

    public void setLovenum(int lovenum) {
        this.lovenum = lovenum;
    }

    public String getBlogtype() {
        return blogtype;
    }

    public void setBlogtype(String blogtype) {
        this.blogtype = blogtype;
    }

    public String getIsreblog() {
        return isreblog;
    }

    public void setIsreblog(String isreblog) {
        this.isreblog = isreblog;
    }

    public String getOrginuno() {
        return orginuno;
    }

    public void setOrginuno(String orginuno) {
        this.orginuno = orginuno;
    }

    public Integer getOrginblogid() {
        return orginblogid;
    }

    public void setOrginblogid(Integer orginblogid) {
        this.orginblogid = orginblogid;
    }

    public Integer getContentnum() {
        return contentnum;
    }

    public void setContentnum(Integer contentnum) {
        this.contentnum = contentnum;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
