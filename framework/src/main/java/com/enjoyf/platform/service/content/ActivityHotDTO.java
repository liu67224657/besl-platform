package com.enjoyf.platform.service.content;

import java.io.Serializable;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class ActivityHotDTO implements Serializable, Comparable<ActivityHotDTO> {
    private Long aid;
    private String pic;
    private String activityName;
    private Long num;

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    @Override
    public int compareTo(ActivityHotDTO o) {
        return this.num> o.getNum()? -1 : (this.num== o.getNum() ? 0 : 1);
    }


    @Override
    public String toString() {
        return "ActivityHotDTO{" +
                "aid=" + aid +
                ", pic='" + pic + '\'' +
                ", activityName='" + activityName + '\'' +
                ", num=" + num +
                '}';
    }
}
