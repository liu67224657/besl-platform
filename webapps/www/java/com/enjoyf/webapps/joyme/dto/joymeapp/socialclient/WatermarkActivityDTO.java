package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-30
 * Time: 下午3:50
 * To change this template use File | Settings | File Templates.
 */
public class WatermarkActivityDTO {

    private WatermarkDTO watermark;

    private ActivityDTO activity;

    public WatermarkDTO getWatermark() {
        return watermark;
    }

    public void setWatermark(WatermarkDTO watermark) {
        this.watermark = watermark;
    }

    public ActivityDTO getActivity() {
        return activity;
    }

    public void setActivity(ActivityDTO activity) {
        this.activity = activity;
    }
}
