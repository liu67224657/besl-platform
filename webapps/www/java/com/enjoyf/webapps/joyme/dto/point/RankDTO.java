package com.enjoyf.webapps.joyme.dto.point;

/**
 * Created by pengxu on 2016/11/28.
 */
public class RankDTO {
    private String pic;
    private String nick;
    private String pid;
    private int value; //积分 or 声望

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
