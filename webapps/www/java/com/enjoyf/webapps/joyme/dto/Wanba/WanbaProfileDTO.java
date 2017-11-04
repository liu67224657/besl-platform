package com.enjoyf.webapps.joyme.dto.Wanba;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/14
 */
public class WanbaProfileDTO {
    public static final int DTO_WANBA_PROFILEID_HASINVITED_STATUS_NO = 0;
    public static final int DTO_WANBA_PROFILEID_HASINVITED_STATUS_YES = 1;

    private String pid;
    private long uid;
    private String nick;
    private String desc;
    private long vtype;
    private String icon;
    private String sex;
    private int point;//提问积分
    private String vdesc;//认证原因
    private String vtitle;//认证title
    private int hasinvited = DTO_WANBA_PROFILEID_HASINVITED_STATUS_NO;

    public int getHasinvited() {
        return hasinvited;
    }

    public void setHasinvited(int hasinvited) {
        this.hasinvited = hasinvited;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getVtype() {
        return vtype;
    }

    public void setVtype(long vtype) {
        this.vtype = vtype;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
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
}
