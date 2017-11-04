package com.enjoyf.platform.webapps.common.util;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/6/17
 * Description:
 */
public class YoukuCookie {
    private String nick;
    private String ytid;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getYtid() {
        return ytid;
    }

    public void setYtid(String ytid) {
        this.ytid = ytid;
    }

    @Override
    public String toString() {
        return "YoukuCookie{" +
                "nickYouku='" + nick + '\'' +
                ", ytid='" + ytid + '\'' +
                '}';
    }
}
