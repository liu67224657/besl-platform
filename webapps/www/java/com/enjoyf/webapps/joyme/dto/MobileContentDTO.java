package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-8-17
 * Time: 下午2:31
 * To change this template use File | Settings | File Templates.
 */
public class MobileContentDTO implements Serializable {
    //文章ID
    private String cid;
    //标题
    private String tt;
    //主体
    private String bd;
    //图片
    private ImageClientSet img;
    //音频
    private MediaDTO ad;
    //视频
    private MediaDTO vd;
    //发布时间
    private Long pdt;
    //热度
    private Integer hot;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public String getBd() {
        return bd;
    }

    public void setBd(String bd) {
        this.bd = bd;
    }

    public ImageClientSet getImg() {
        return img;
    }

    public void setImg(ImageClientSet img) {
        this.img = img;
    }

    public MediaDTO getAd() {
        return ad;
    }

    public void setAd(MediaDTO ad) {
        this.ad = ad;
    }

    public MediaDTO getVd() {
        return vd;
    }

    public void setVd(MediaDTO vd) {
        this.vd = vd;
    }

    public Long getPdt() {
        return pdt;
    }

    public void setPdt(Long pdt) {
        this.pdt = pdt;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    //to string
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
