package com.enjoyf.platform.service.gameres;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-10-23
 * Time: 下午3:39
 * To change this template use File | Settings | File Templates.
 */
public class GameMediaScore implements Serializable{

    private String from;
    private String score;
    private String description;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
