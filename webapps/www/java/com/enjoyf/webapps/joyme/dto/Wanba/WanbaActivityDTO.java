package com.enjoyf.webapps.joyme.dto.Wanba;

import java.util.List;

/**
 * Created by zhimingli on 2016/9/26 0026.
 */
public class WanbaActivityDTO {
    private String tagid;
    private String title;//标题
    private String pic; //头图
    private String corner;//角标文本
    private String desc;//描述
    private List<WanbaProfileDTO> profiledtos;

    private WanbaShareDTO share;

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCorner() {
        return corner;
    }

    public void setCorner(String corner) {
        this.corner = corner;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public List<WanbaProfileDTO> getProfiledtos() {
        return profiledtos;
    }

    public void setProfiledtos(List<WanbaProfileDTO> profiledtos) {
        this.profiledtos = profiledtos;
    }

    public WanbaShareDTO getShare() {
        return share;
    }

    public void setShare(WanbaShareDTO share) {
        this.share = share;
    }
}
