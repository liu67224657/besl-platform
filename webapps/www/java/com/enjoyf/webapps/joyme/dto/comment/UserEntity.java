package com.enjoyf.webapps.joyme.dto.comment;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-12
 * Time: 下午9:23
 * To change this template use File | Settings | File Templates.
 */
public class UserEntity {

    private String uno;
    private long uid;
    private String name;
    private String sex;
    private String icon;
    private String domain;
    private String verify;
    private String pid;
    private String headskin = ""; //头像框
    private String replyskin = ""; //评论框
    private String cardskin = "";  //名片框
    private String bubbleskin = ""; //聊天气泡


    private String vdesc = ""; //认证原因
    private String vtitle = "";  //认证title
    private long vtype = 0; //认证类型


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getHeadskin() {
        return headskin;
    }

    public void setHeadskin(String headskin) {
        this.headskin = headskin;
    }

    public String getReplyskin() {
        return replyskin;
    }

    public void setReplyskin(String replyskin) {
        this.replyskin = replyskin;
    }

    public String getCardskin() {
        return cardskin;
    }

    public void setCardskin(String cardskin) {
        this.cardskin = cardskin;
    }

    public String getBubbleskin() {
        return bubbleskin;
    }

    public void setBubbleskin(String bubbleskin) {
        this.bubbleskin = bubbleskin;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getVdesc() {
        return vdesc;
    }

    public void setVdesc(String vdesc) {
        this.vdesc = vdesc;
    }

    public String getVtitle() {
        return vtitle;
    }

    public void setVtitle(String vtitle) {
        this.vtitle = vtitle;
    }

    public long getVtype() {
        return vtype;
    }

    public void setVtype(long vtype) {
        this.vtype = vtype;
    }
}
