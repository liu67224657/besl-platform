package com.enjoyf.webapps.joyme.webpage.controller.comment;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.util.HttpClientManager;
import com.enjoyf.util.HttpParameter;

/**
 * Created by zhitaoshi on 2016/2/3.
 */
public class CommentReportWebcacheThread extends Thread {

    private String unikey;
    private String domain;

    public CommentReportWebcacheThread(String unikey, String domain) {
        this.unikey = unikey;
        this.domain = domain;
    }

    @Override
    public void run() {
        if (StringUtil.isEmpty(unikey) || StringUtil.isEmpty(domain)) {
            return;
        }
        String pageId = "";
        String pageType = "";// 1 -- cms, 2 -- wiki
        if (CommentDomain.CMS_COMMENT.equals(CommentDomain.getByCode(Integer.valueOf(domain)))) {
            pageId = unikey;
            pageType = "1";
        } else if (CommentDomain.DIGITAL_COMMENT.equals(CommentDomain.getByCode(Integer.valueOf(domain)))) {
            if (unikey.indexOf(".shtml") > 0) {
                pageId = unikey.substring(0, unikey.indexOf(".shtml"));
            }
            pageType = "2";
        }
        if (!StringUtil.isEmpty(pageId) && !StringUtil.isEmpty(pageType)) {
            HttpClientManager httpClientManager = new HttpClientManager();
            String commentUrl = "http://webcache."+WebappConfig.get().getDomain()+"/comment/report/reply.do";
            httpClientManager.get(commentUrl, new HttpParameter[]{
                    new HttpParameter("pageid", pageId),
                    new HttpParameter("pagetype", pageType)
            }, "UTF-8");
        }
    }
}
