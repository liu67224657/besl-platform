package com.enjoyf.platform.service.weixin.resp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-5-14
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 * 回复图文消息
 */
public class RespNewsMessage extends RespBaseMessage{
     // 图文消息个数，限制为10条以内
    private int ArticleCount;
    // 多条图文消息信息，默认第一个item为大图
    private List<Article> Articles;

    public int getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount) {
        ArticleCount = articleCount;
    }

    public List<Article> getArticles() {
        return Articles;
    }

    public void setArticles(List<Article> articles) {
        Articles = articles;
    }
}
