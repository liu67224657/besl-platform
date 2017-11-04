package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-20
 * Time: 下午3:10
 * To change this template use File | Settings | File Templates.
 */
public class WikiVote implements Serializable{

    private long articleId;
    private String url;
    private String name;
    private String pic;
    private String noStr;
    private String keyWords;

    private int votesSum;

    private Date createDate;
    private Date modifyDate;
    private String modifyUserId;
    private ActStatus removeStatus = ActStatus.UNACT;

    public WikiVote() {
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getNoStr() {
        return noStr;
    }

    public void setNoStr(String noStr) {
        this.noStr = noStr;
    }

    public int getVotesSum() {
        return votesSum;
    }

    public void setVotesSum(int votesSum) {
        this.votesSum = votesSum;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    @Override
    public int hashCode() {
        return (int) this.articleId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
