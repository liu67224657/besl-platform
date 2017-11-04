package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-12-5
 * Time: 上午9:55
 * To change this template use File | Settings | File Templates.
 */
public class GameDBChannelDownloadInfo implements Serializable {
    private String download;        //下载地址
    private String gameversion;    //游戏版本
    private String gamesize;        //游戏大小
    private String systemversion;   //系统版本

    private String channelName;  //接口使用 其他不用;
    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getGameversion() {
        return gameversion;
    }

    public void setGameversion(String gameversion) {
        this.gameversion = gameversion;
    }

    public String getGamesize() {
        return gamesize;
    }

    public void setGamesize(String gamesize) {
        this.gamesize = gamesize;
    }

    public String getSystemversion() {
        return systemversion;
    }

    public void setSystemversion(String systemversion) {
        this.systemversion = systemversion;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
