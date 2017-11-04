package com.enjoyf.webapps.joyme.dto.activity;

import java.io.Serializable;

/**
 * Created by zhitaoshi on 2015/12/8.
 */
public class Shuang12DTO implements Serializable {

    private long uid;
    private String nick;
    private String words;

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

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}
