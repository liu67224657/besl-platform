package com.enjoyf.webapps.tools.weblogic.mail;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * <p/>
 * Description:邮件信息的封装类。
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class MailInfo implements Serializable {
    private String fromAdd;//发信人地址
    private String fromName;//发信人名称
    private String[] to;
    private String[] toName;//收信人名称
    private String subject;

    private Map<String, Object> paramMap;
    private String ftlUrl;
    private String content;

    public String getFromAdd() {
        return fromAdd;
    }

    public void setFromAdd(String fromAdd) {
        this.fromAdd = fromAdd;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getToName() {
        return toName;
    }

    public void setToName(String[] toName) {
        this.toName = toName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFtlUrl() {
        return ftlUrl;
    }

    public void setFtlUrl(String ftlUrl) {
        this.ftlUrl = ftlUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
