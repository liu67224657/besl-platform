package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-8-9
 * Time: 下午2:58
 * To change this template use File | Settings | File Templates.
 */
public class ContentClientDTO implements Serializable {
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
    //文章ID
    private String cid;
    //热度
    private Integer hot;
    //转发的原文
    private ContentClientDTO root;
    //是否喜欢,1表示已经喜欢，0表示未喜欢
    private Integer fav;
    //喜欢时间
    private Long fdt;

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

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public ContentClientDTO getRoot() {
        return root;
    }

    public void setRoot(ContentClientDTO root) {
        this.root = root;
    }

    public Integer getFav() {
        return fav;
    }

    public void setFav(Integer fav) {
        this.fav = fav;
    }

    public Long getFdt() {
        return fdt;
    }

    public void setFdt(Long fdt) {
        this.fdt = fdt;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public String toJsonStr() {

        return JsonBinder.buildNormalBinder().toJson(this);

    }

    public static void main(String[] args) {
        System.out.println(new ContentClientDTO().toJsonStr());
    }
}
