package com.enjoyf.platform.serv.comment;/**
 * Created by ericliu on 16/8/9.
 */

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.util.HttpClientManager;
import com.enjoyf.util.HttpParameter;

import java.io.Serializable;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/8/9
 */
class WebCacheWrap implements Serializable {

    private String unikey;
    private String domain;
    private int incNum;

    public WebCacheWrap(String unikey, String domain, int incNum) {
        this.unikey = unikey;
        this.domain = domain;
        this.incNum = incNum;
    }

    public void processWebcache() {
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
            String commentUrl = "http://webcache." + WebappConfig.get().getDomain() + "/comment/report/reply.do";
            httpClientManager.get(commentUrl, new HttpParameter[]{
                    new HttpParameter("pageid", pageId),
                    new HttpParameter("pagetype", pageType),
                    new HttpParameter("incnum", incNum)
            }, "UTF-8");
        }
    }

}
