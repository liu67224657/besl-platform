package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-19
 * Time: 下午12:16
 * To change this template use File | Settings | File Templates.
 */
public class SocialContentReplyDTO {
    private long replyid;
    private String replyuno;

    private long cid;
    private String cuno;

    private long pid;
    private String puno;
    private String pname;

    private long rid;
    private String runo;
    private String rname;

    private String body;

    private long createtime;
    private String ip;

    private float lon;
    private float lat;


    private String headicon; //头像

    private String sex; //性别

    private String birthday;  //生日

    private String name;//昵称


    public long getReplyid() {
        return replyid;
    }

    public void setReplyid(long replyid) {
        this.replyid = replyid;
    }

    public String getReplyuno() {
        return replyuno;
    }

    public void setReplyuno(String replyuno) {
        this.replyuno = replyuno;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getCuno() {
        return cuno;
    }

    public void setCuno(String cuno) {
        this.cuno = cuno;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getPuno() {
        return puno;
    }

    public void setPuno(String puno) {
        this.puno = puno;
    }

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public String getHeadicon() {
        return headicon;
    }

    public void setHeadicon(String headicon) {
        this.headicon = headicon;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }
}
