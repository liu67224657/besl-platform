package com.enjoyf.platform.thirdapi.friends;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-3
 * Time: 下午6:18
 * To change this template use File | Settings | File Templates.
 */
public class FriendUrlParam implements Serializable {
    private String source;  //数值为应用的AppKey
    private String access_token;//OAuth授权后获得
    private String q;//搜索的关键字，必须做URLencoding。
    private String count;//返回的记录条数
    private String type;//联想类型，0：关注、1：粉丝。
    private String uid;//需要查询的用户UID。
    private String screen_name;//需要查询的用户昵称。
    private String cursor;//返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0。

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
