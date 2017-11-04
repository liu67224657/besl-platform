package com.enjoyf.platform.cloud.contentservice.game;


import com.enjoyf.platform.cloud.enumeration.ValidStatus;

import java.io.Serializable;

/**
 * A GameTag.
 */
public class GameTag implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String tagName;

    private Integer gameSum = 0;

    private Integer recommendStatus = 0;

    private ValidStatus validStatus = ValidStatus.VALID;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public GameTag tagName(String tagName) {
        this.tagName = tagName;
        return this;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getGameSum() {
        return gameSum;
    }

    public GameTag gameSum(Integer gameSum) {
        this.gameSum = gameSum;
        return this;
    }

    public void setGameSum(Integer gameSum) {
        this.gameSum = gameSum;
    }

    public Integer getRecommendStatus() {
        return recommendStatus;
    }

    public GameTag recommendStatus(Integer recommendStatus) {
        this.recommendStatus = recommendStatus;
        return this;
    }

    public void setRecommendStatus(Integer recommendStatus) {
        this.recommendStatus = recommendStatus;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    @Override
    public String toString() {
        return "GameTag{" +
                "id=" + getId() +
                ", tagName='" + getTagName() + "'" +
                ", gameSum='" + getGameSum() + "'" +
                ", recommendStatus='" + getRecommendStatus() + "'" +
                ", validStatus='" + getValidStatus() + "'" +
                "}";
    }
}
