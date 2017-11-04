package com.enjoyf.webapps.joyme.dto.comment;

import com.enjoyf.platform.util.PageRows;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-25
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
public class CommentResult {

    private String uri;  //链接地址
    private String title; //标题
    private String pic; // 图片
    private String description;   //描述
    private int comment_sum;   //评论总数
    private int share_sum;

    private UserEntity user;//文章、帖子、投票的  发起人/作者

    private PageRows<MainReplyDTO> mainreplys;  //评论列表
    private List<MainReplyDTO> hotlist;//热门列表

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getComment_sum() {
        return comment_sum;
    }

    public void setComment_sum(int comment_sum) {
        this.comment_sum = comment_sum;
    }

    public PageRows<MainReplyDTO> getMainreplys() {
        return mainreplys;
    }

    public void setMainreplys(PageRows<MainReplyDTO> mainreplys) {
        this.mainreplys = mainreplys;
    }

    public List<MainReplyDTO> getHotlist() {
        return hotlist;
    }

    public void setHotlist(List<MainReplyDTO> hotlist) {
        this.hotlist = hotlist;
    }

    public int getShare_sum() {
        return share_sum;
    }

    public void setShare_sum(int share_sum) {
        this.share_sum = share_sum;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
