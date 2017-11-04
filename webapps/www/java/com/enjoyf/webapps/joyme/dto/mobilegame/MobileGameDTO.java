package com.enjoyf.webapps.joyme.dto.mobilegame;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-9-12
 * Time: 下午4:30
 * To change this template use File | Settings | File Templates.
 */
public class MobileGameDTO implements Serializable {
    private long gamedbid;
    private String gamename;  //游戏名称
    private String gameicon;  //游戏主图
    private double average_score;
    private double average_score_per;//总榜百分比
    private int replynum; //排行榜评论数
    private long contentid;
    private long lineid;//在哪个排行榜里面
    private List<MobileGameUserDTO> shortcommentlist;


    //下载页用到的
    private String device; //设备ID 1.iphone 2.IPAD 3。android
    private String channelName;// 渠道名称
    private String downloadAddress;//下载地址

    public long getGamedbid() {
        return gamedbid;
    }

    public void setGamedbid(long gamedbid) {
        this.gamedbid = gamedbid;
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public String getGameicon() {
        return gameicon;
    }

    public void setGameicon(String gameicon) {
        this.gameicon = gameicon;
    }

    public double getAverage_score() {
        return average_score;
    }

    public void setAverage_score(double average_score) {
        this.average_score = average_score;
    }

    public double getAverage_score_per() {
        return average_score_per;
    }

    public void setAverage_score_per(double average_score_per) {
        this.average_score_per = average_score_per;
    }

    public int getReplynum() {
        return replynum;
    }

    public void setReplynum(int replynum) {
        this.replynum = replynum;
    }

    public long getContentid() {
        return contentid;
    }

    public void setContentid(long contentid) {
        this.contentid = contentid;
    }

    public long getLineid() {
        return lineid;
    }

    public void setLineid(long lineid) {
        this.lineid = lineid;
    }

    public List<MobileGameUserDTO> getShortcommentlist() {
        return shortcommentlist;
    }

    public void setShortcommentlist(List<MobileGameUserDTO> shortcommentlist) {
        this.shortcommentlist = shortcommentlist;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getDownloadAddress() {
        return downloadAddress;
    }

    public void setDownloadAddress(String downloadAddress) {
        this.downloadAddress = downloadAddress;
    }




    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
