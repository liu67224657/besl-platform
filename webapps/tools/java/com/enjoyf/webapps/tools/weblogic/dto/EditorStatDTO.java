package com.enjoyf.webapps.tools.weblogic.dto;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-4-1
 * Time: 下午1:48
 * To change this template use File | Settings | File Templates.
 */
public class EditorStatDTO {
    private int adminUno;
    private String editorName;
    private long pingce;
    private long gonglue;
    private long zhuanti;

    private long pv;
    private long uv;
    private long cpv;
    private long cmt;
    private long post;
    private long gamepv;
    private long gameuv;
    private long postGame;


    public int getAdminUno() {
        return adminUno;
    }

    public void setAdminUno(int adminUno) {
        this.adminUno = adminUno;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public long getPingce() {
        return pingce;
    }

    public void setPingce(long pingce) {
        this.pingce = pingce;
    }

    public long getGonglue() {
        return gonglue;
    }

    public void setGonglue(long gonglue) {
        this.gonglue = gonglue;
    }

    public long getZhuanti() {
        return zhuanti;
    }

    public void setZhuanti(long zhuanti) {
        this.zhuanti = zhuanti;
    }

    public long getPv() {
        return pv;
    }

    public void setPv(long pv) {
        this.pv = pv;
    }

    public long getUv() {
        return uv;
    }

    public void setUv(long uv) {
        this.uv = uv;
    }

    public long getCpv() {
        return cpv;
    }

    public void setCpv(long cpv) {
        this.cpv = cpv;
    }

    public long getCmt() {
        return cmt;
    }

    public void setCmt(long cmt) {
        this.cmt = cmt;
    }

    public long getPost() {
        return post;
    }

    public void setPost(long post) {
        this.post = post;
    }

    public long getGamepv() {
        return gamepv;
    }

    public void setGamepv(long gamepv) {
        this.gamepv = gamepv;
    }

    public long getGameuv() {
        return gameuv;
    }

    public void setGameuv(long gameuv) {
        this.gameuv = gameuv;
    }

    public long getPostGame() {
        return postGame;
    }

    public void setPostGame(long postGame) {
        this.postGame = postGame;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
