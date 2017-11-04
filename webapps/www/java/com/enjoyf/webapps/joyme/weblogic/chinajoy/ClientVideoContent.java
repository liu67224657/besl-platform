package com.enjoyf.webapps.joyme.weblogic.chinajoy;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-5-25
 * Time: 下午3:40
 * To change this template use File | Settings | File Templates.
 */
public class ClientVideoContent implements Serializable {
    private String title;//标题
    private String desc;//描述
    private String src;//图片地址
    private String orgUrl;//原始地址

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getOrgUrl() {
        return orgUrl;
    }

    public void setOrgUrl(String orgUrl) {
        this.orgUrl = orgUrl;
    }
}
