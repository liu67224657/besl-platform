package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-8-20
 * Time: 上午10:26
 * To change this template use File | Settings | File Templates.
 */
public class MobileViewLineItemContentDTO implements Serializable {
    //文章ID
    private String cid;
    //文章作者uno
    private String uno;
    //标题
    private String tt;
    //主体
    private String bd;
    //图片
    private MediaDTO img;
    //热度
    private Integer hot;
    //发布日期
    private Long pdt;

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

    public MediaDTO getImg() {
        return img;
    }

    public void setImg(MediaDTO img) {
        this.img = img;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public Long getPdt() {
        return pdt;
    }

    public void setPdt(Long pdt) {
        this.pdt = pdt;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    //to string
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
