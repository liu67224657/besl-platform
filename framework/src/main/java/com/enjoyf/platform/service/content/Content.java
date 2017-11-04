/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ContentTag;
import com.enjoyf.platform.service.PrivacyType;
import com.enjoyf.platform.service.tools.ContentAuditStatus;
import com.enjoyf.platform.service.vote.Vote;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午5:21
 * Description: 按照authorUno进行分类。
 */
public class Content implements Serializable {
    //sequence id.
    private String contentId;

    //author uno
    private String uno;

    //主题
    private String subject;

    //标签
    private ContentTag contentTag = new ContentTag();

    //用户隐藏标签，用于搜索
    private ContentTag searchTag = new ContentTag();

    //内容
    private String content;

    //  图片，声音，视频
    private ImageContentSet images;
    private AudioContentSet audios;
    private VideoContentSet videos;
    private AppsContentSet apps;
    private GameContentSet games;

    // 投票
    private Vote vote;

    // vote
    private String voteSubjectId;

    private String thumbImgLink;   //缩略图地址

    //blog type:原创，转发
    private ContentPublishType publishType = ContentPublishType.ORIGINAL;

    //内容类型：一句话，图片；文章；etc
    private ContentType contentType = new ContentType();

    //隐私设置
    private PrivacyType privacy = PrivacyType.PUBLIC;

    // 发布时间，IP
    private Date publishDate;
    private String publishIp;

    //转发时为原文ID。
    private String rootContentId;
    private String rootContentUno;

    //parent content info.
    private String parentContentId;
    private String parentContentUno;

    //stats info
    private int replyTimes = 0;
    private int forwardTimes = 0;
    private int favorTimes = 0;
    //顶->extnum01
    private Integer dingTimes = 0;
    //cai -> extnum02
    private int caiTimes = 0;
    //view -> extnum03
    private int viewTimes = 0;
    //floor-> extnum04
    private int floorTimes = 0;

    //last reply info -> extstr01
    private String lastReplyId;

    //todo  boardId --> extstr02
//    private String resourceId;

    //not store db
//    private long groupResourceId;
    private ContentRelationSet relationSet;

    //
    private Date updateDate;//更新日期

    //伪删除标记
    private ActStatus removeStatus = ActStatus.UNACT;

    //审核标记
    private ContentAuditStatus auditStatus;

    private SyncSet syncSet = new SyncSet();
    private String contentUrl;

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public void setDingTimes(Integer dingTimes) {
        this.dingTimes = dingTimes;
    }

    //
    public Content() {
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ContentTag getContentTag() {
        return contentTag;
    }

    public void setContentTag(ContentTag contentTag) {
        this.contentTag = contentTag;
    }

    public ContentTag getSearchTag() {
        return searchTag;
    }

    public void setSearchTag(ContentTag searchTag) {
        this.searchTag = searchTag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ImageContentSet getImages() {
        return images;
    }

    public void setImages(ImageContentSet images) {
        this.images = images;
    }

    public AudioContentSet getAudios() {
        return audios;
    }

    public void setAudios(AudioContentSet audios) {
        this.audios = audios;
    }

    public VideoContentSet getVideos() {
        return videos;
    }

    public void setVideos(VideoContentSet videos) {
        this.videos = videos;
    }

    public AppsContentSet getApps() {
        return apps;
    }

    public void setApps(AppsContentSet apps) {
        this.apps = apps;
    }

    public GameContentSet getGames() {
        return games;
    }

    public void setGames(GameContentSet games) {
        this.games = games;
    }

    public String getVoteSubjectId() {
        return voteSubjectId;
    }

    public void setVoteSubjectId(String voteSubjectId) {
        this.voteSubjectId = voteSubjectId;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public String getThumbImgLink() {
        return thumbImgLink;
    }

    public void setThumbImgLink(String thumbImgLink) {
        this.thumbImgLink = thumbImgLink;
    }

    public ContentPublishType getPublishType() {
        return publishType;
    }

    public void setPublishType(ContentPublishType publishType) {
        this.publishType = publishType;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public PrivacyType getPrivacy() {
        return privacy;
    }

    public void setPrivacy(PrivacyType privacy) {
        this.privacy = privacy;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishIp() {
        return publishIp;
    }

    public void setPublishIp(String publishIp) {
        this.publishIp = publishIp;
    }

    public String getRootContentId() {
        return rootContentId;
    }

    public void setRootContentId(String rootContentId) {
        this.rootContentId = rootContentId;
    }

    public String getRootContentUno() {
        return rootContentUno;
    }

    public void setRootContentUno(String rootContentUno) {
        this.rootContentUno = rootContentUno;
    }

    public String getParentContentId() {
        return parentContentId;
    }

    public void setParentContentId(String parentContentId) {
        this.parentContentId = parentContentId;
    }

    public String getParentContentUno() {
        return parentContentUno;
    }

    public void setParentContentUno(String parentContentUno) {
        this.parentContentUno = parentContentUno;
    }

    public int getReplyTimes() {
        return replyTimes;
    }

    public void setReplyTimes(int replyTimes) {
        this.replyTimes = replyTimes;
    }

    public int getForwardTimes() {
        return forwardTimes;
    }

    public void setForwardTimes(int forwardTimes) {
        this.forwardTimes = forwardTimes;
    }

    public int getFavorTimes() {
        return favorTimes;
    }

    public void setFavorTimes(int favorTimes) {
        this.favorTimes = favorTimes;
    }

    public Integer getDingTimes() {
        return dingTimes;
    }

    public void setDingTimes(int dingTimes) {
        this.dingTimes = dingTimes;
    }

    public int getCaiTimes() {
        return caiTimes;
    }

    public void setCaiTimes(int caiTimes) {
        this.caiTimes = caiTimes;
    }

    public int getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(int viewTimes) {
        this.viewTimes = viewTimes;
    }

    public String getLastReplyId() {
        return lastReplyId;
    }

    public void setLastReplyId(String lastReplyId) {
        this.lastReplyId = lastReplyId;
    }

    public ContentRelationSet getRelationSet() {
        return relationSet;
    }

    public void setRelationSet(ContentRelationSet relationSet) {
        this.relationSet = relationSet;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public SyncSet getSyncSet() {
        return syncSet;
    }

    public ContentAuditStatus getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(ContentAuditStatus auditStatus) {
        this.auditStatus = auditStatus;
    }

    public void setSyncSet(SyncSet syncSet) {
        this.syncSet = syncSet;
    }

    public int getFloorTimes() {
        return floorTimes;
    }

    public void setFloorTimes(int floorTimes) {
        this.floorTimes = floorTimes;
    }


    @Override
    public int hashCode() {
        return contentId != null ? contentId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
