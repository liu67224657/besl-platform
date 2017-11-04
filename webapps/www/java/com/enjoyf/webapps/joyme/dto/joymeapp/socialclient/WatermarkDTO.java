package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-28
 * Time: 下午7:35
 * To change this template use File | Settings | File Templates.
 */
public class WatermarkDTO {

    private long watermarkid;  //水印ID
    private String title;   //标题
    private String description;  //描述
    private String icon; //正方形 小图标
    private String pic;  //图片
    private SubscriptDTO subscript;  //角标
    private int usesum = 0;
    private Long activityid;

    public long getWatermarkid() {
        return watermarkid;
    }

    public void setWatermarkid(long watermarkid) {
        this.watermarkid = watermarkid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public SubscriptDTO getSubscript() {
        return subscript;
    }

    public void setSubscript(SubscriptDTO subscript) {
        this.subscript = subscript;
    }

    public int getUsesum() {
        return usesum;
    }

    public void setUsesum(int usesum) {
        this.usesum = usesum;
    }

    public Long getActivityid() {
        return activityid;
    }

    public void setActivityid(Long activityid) {
        this.activityid = activityid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
