package com.enjoyf.platform.webapps.common.dto.index;

import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.service.viewline.ViewLine;
import com.enjoyf.platform.util.PageRows;


/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-7
 * Time: 下午1:37
 * To change this template use File | Settings | File Templates.
 */
public class ActitvityPageDTO {
    private String recommendActivity;
    private String bulletin;

    private ViewLine activityListLine;

    public String getRecommendActivity() {
        return recommendActivity;
    }

    public void setRecommendActivity(String recommendActivity) {
        this.recommendActivity = recommendActivity;
    }

    public String getBulletin() {
        return bulletin;
    }

    public void setBulletin(String bulletin) {
        this.bulletin = bulletin;
    }

    public ViewLine getActivityListLine() {
        return activityListLine;
    }

    public void setActivityListLine(ViewLine activityListLine) {
        this.activityListLine = activityListLine;
    }
}
