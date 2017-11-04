package com.enjoyf.platform.service.joymeapp.config;

import java.io.Serializable;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/3/19
 * Description:
 */
public class ShakeConfig implements Serializable {

    private String bgpic = "";
    private String title = "";
    private String tag = "";
    private String tagcolor = "";
    private long tagbegintime;
    private long tagendtime;
    private String buttontext;
    private int shakeType;
    private ShakeRange shakeRange = new ShakeRange(0, 100);

    private int shaketimes; //0无限制 1用户一次
    private boolean timelimit = false; //是否有时间属性
    private long begintime;
    private long endtime;


    public String getBgpic() {
        return bgpic;
    }

    public void setBgpic(String bgpic) {
        this.bgpic = bgpic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTagcolor() {
        return tagcolor;
    }

    public void setTagcolor(String tagcolor) {
        this.tagcolor = tagcolor;
    }

    public String getButtontext() {
        return buttontext;
    }

    public void setButtontext(String buttontext) {
        this.buttontext = buttontext;
    }

    public int getShaketimes() {
        return shaketimes;
    }

    public void setShaketimes(int shaketimes) {
        this.shaketimes = shaketimes;
    }

    public boolean isTimelimit() {
        return timelimit;
    }

    public void setTimelimit(boolean isTimeLimit) {
        this.timelimit = isTimeLimit;
    }

    public long getBegintime() {
        return begintime;
    }

    public void setBegintime(long begintime) {
        this.begintime = begintime;
    }

    public long getTagbegintime() {
        return tagbegintime;
    }

    public void setTagbegintime(long tagbegintime) {
        this.tagbegintime = tagbegintime;
    }

    public long getTagendtime() {
        return tagendtime;
    }

    public void setTagendtime(long tagendtime) {
        this.tagendtime = tagendtime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public int getShakeType() {
        return shakeType;
    }

    public void setShakeType(int shakeType) {
        this.shakeType = shakeType;
    }

    public ShakeRange getShakeRange() {
        return shakeRange;
    }

    public void setShakeRange(ShakeRange shakeRange) {
        this.shakeRange = shakeRange;
    }
}
