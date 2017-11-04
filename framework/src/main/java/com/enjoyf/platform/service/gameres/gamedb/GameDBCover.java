package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by zhimingli
 * Date: 2015/01/13
 * Time: 19:32
 */
public class GameDBCover implements Serializable {
    private String coverPicUrl;//游戏封面图
    private String coverTitle;//游戏标题
    private String coverComment;//游戏点评
    private String coverDesc;//游戏详情
    private String coverAgreeNum;//点赞数
    private String coverDownload;//是否显示下载---IOS
    private String coverDownloadAndroid;//是否显示下载--Android

    private String posterShowTypeIos;        //新游开测显示方式 1:精确时间，2:自定义显示文字
    private String posterShowContentIos;     //新游开测自定义显示内容
    private String posterGamePublicTimeIos;   //新游开测的上市时间

    private String posterShowTypeAndroid;        //新游开测显示方式 1:精确时间，2:自定义显示文字
    private String posterShowContentAndroid;     //新游开测自定义显示内容
    private String posterGamePublicTimeAndroid;   //新游开测的上市时间

    public String getPosterShowTypeIos() {
        return posterShowTypeIos;
    }

    public void setPosterShowTypeIos(String posterShowTypeIos) {
        this.posterShowTypeIos = posterShowTypeIos;
    }

    public String getPosterShowContentIos() {
        return posterShowContentIos;
    }

    public void setPosterShowContentIos(String posterShowContentIos) {
        this.posterShowContentIos = posterShowContentIos;
    }

    public String getPosterGamePublicTimeIos() {
        return posterGamePublicTimeIos;
    }

    public void setPosterGamePublicTimeIos(String posterGamePublicTimeIos) {
        this.posterGamePublicTimeIos = posterGamePublicTimeIos;
    }

    public String getPosterShowTypeAndroid() {
        return posterShowTypeAndroid;
    }

    public void setPosterShowTypeAndroid(String posterShowTypeAndroid) {
        this.posterShowTypeAndroid = posterShowTypeAndroid;
    }

    public String getPosterShowContentAndroid() {
        return posterShowContentAndroid;
    }

    public void setPosterShowContentAndroid(String posterShowContentAndroid) {
        this.posterShowContentAndroid = posterShowContentAndroid;
    }

    public String getPosterGamePublicTimeAndroid() {
        return posterGamePublicTimeAndroid;
    }

    public void setPosterGamePublicTimeAndroid(String posterGamePublicTimeAndroid) {
        this.posterGamePublicTimeAndroid = posterGamePublicTimeAndroid;
    }

    public String getCoverPicUrl() {
        return coverPicUrl;
    }

    public void setCoverPicUrl(String coverPicUrl) {
        this.coverPicUrl = coverPicUrl;
    }

    public String getCoverTitle() {
        return coverTitle;
    }

    public void setCoverTitle(String coverTitle) {
        this.coverTitle = coverTitle;
    }

    public String getCoverComment() {
        return coverComment;
    }

    public void setCoverComment(String coverComment) {
        this.coverComment = coverComment;
    }

    public String getCoverDesc() {
        return coverDesc;
    }

    public void setCoverDesc(String coverDesc) {
        this.coverDesc = coverDesc;
    }

    public String getCoverAgreeNum() {
        return coverAgreeNum;
    }

    public void setCoverAgreeNum(String coverAgreeNum) {
        this.coverAgreeNum = coverAgreeNum;
    }

    public String getCoverDownload() {
        return coverDownload;
    }

    public void setCoverDownload(String coverDownload) {
        this.coverDownload = coverDownload;
    }

    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static GameDBCover fromJson(String jsonString) {

        GameDBCover returnObj = new GameDBCover();

        try {
            if (!StringUtil.isEmpty(jsonString)) {
                returnObj = JsonBinder.buildNonNullBinder().getMapper().readValue(jsonString, new TypeReference<GameDBCover>() {
                });
            }
        } catch (IOException e) {
            GAlerter.lab(GameDBCover.class.getName() + " occured IOException.e", e);
        }
        return returnObj;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public String getCoverDownloadAndroid() {
        return coverDownloadAndroid;
    }

    public void setCoverDownloadAndroid(String coverDownloadAndroid) {
        this.coverDownloadAndroid = coverDownloadAndroid;
    }
}
