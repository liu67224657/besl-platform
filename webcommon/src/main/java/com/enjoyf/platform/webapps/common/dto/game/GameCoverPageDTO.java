package com.enjoyf.platform.webapps.common.dto.game;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class GameCoverPageDTO {
    private String recommend;
    private String advertise;
    private String news;
    private String raiders;
    private String image;
    private String video;

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getAdvertise() {
        return advertise;
    }

    public void setAdvertise(String advertise) {
        this.advertise = advertise;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getRaiders() {
        return raiders;
    }

    public void setRaiders(String raiders) {
        this.raiders = raiders;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
