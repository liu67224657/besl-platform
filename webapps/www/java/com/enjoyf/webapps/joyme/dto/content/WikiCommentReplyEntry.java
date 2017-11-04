package com.enjoyf.webapps.joyme.dto.content;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ForignContentDomain;
import com.enjoyf.platform.service.content.ForignContentReply;
import com.enjoyf.webapps.joyme.dto.profile.ProfileMiniDTO;

import java.util.Date;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-20 下午5:59
 * Description:
 */
public class WikiCommentReplyEntry {
    private long rid;//评论ID
    private String runo;

    private long cid;//文章的ID
    private String puno; //楼中楼 上级回复 用户uno
    private long pid;//楼中楼 上级回复 ID
    private String ouno; //主楼评论 用户uno
    private long oid;//主楼评论 ID

    private int rsum;//回复数
    private int asum;//支持数
    private int disasum;//不支持数
    private String body;
    private String pic;
    private int floornum;
    private Date rdate;
    private double score;//评分

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public String getRuno() {
        return runo;
    }

    public void setRuno(String runo) {
        this.runo = runo;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getPuno() {
        return puno;
    }

    public void setPuno(String puno) {
        this.puno = puno;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getOuno() {
        return ouno;
    }

    public void setOuno(String ouno) {
        this.ouno = ouno;
    }

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public int getRsum() {
        return rsum;
    }

    public void setRsum(int rsum) {
        this.rsum = rsum;
    }

    public int getAsum() {
        return asum;
    }

    public void setAsum(int asum) {
        this.asum = asum;
    }

    public int getDisasum() {
        return disasum;
    }

    public void setDisasum(int disasum) {
        this.disasum = disasum;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getFloornum() {
        return floornum;
    }

    public void setFloornum(int floornum) {
        this.floornum = floornum;
    }

    public Date getRdate() {
        return rdate;
    }

    public void setRdate(Date rdate) {
        this.rdate = rdate;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
