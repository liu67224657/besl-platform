package com.enjoyf.webapps.joyme.dto.activity;

import java.util.Date;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class ActivityDTO {
    private String gipic;  //icon
    private String title;  //标题
    private String desc;  //描述
    private int rn;    //已领多少
    private int cn;    //总数
    private Long gid;   //活动ID
    private int point;//积分兑换 goods 的 价格
    private int sn;//剩余数量
    private Long geid;//goods 或  exchangegoods id
    private Date exDate;//过期时间
    private Long sid;//分享shareId
    private int weixinExclusive;
    private Long shuang11Point;//扩展字段 双11 折前价格
    private String shuang11Pic;
    private boolean isNews; //是否最新
    private boolean isHot; //是否最热
    private int reserveType;
    private long consumeOrder;//dingdanhao
    private Date exchangeTime;
    private int hotActivity; //是否是热门活动 0=不是 1=是
    private Date startDate; //秒杀开始时间
    private int seckillType;
    ///////优酷
    private String yktitle;//标题
    private String ykdesc; //描述
    private String ykpic;  //图片
    private String ipadpic;//PAD图片
    private String supscript; //角标
    private String color;     //角标颜色
    private String price;
    private int platform;

    public String getIpadpic() {
        return ipadpic;
    }

    public void setIpadpic(String ipadpic) {
        this.ipadpic = ipadpic;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getConsumeOrder() {
        return consumeOrder;
    }

    public void setConsumeOrder(long consumeOrder) {
        this.consumeOrder = consumeOrder;
    }

    public String getGipic() {
        return gipic;
    }

    public void setGipic(String gipic) {
        this.gipic = gipic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getRn() {
        return rn;
    }

    public void setRn(int rn) {
        this.rn = rn;
    }

    public int getCn() {
        return cn;
    }

    public void setCn(int cn) {
        this.cn = cn;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public Long getGeid() {
        return geid;
    }

    public void setGeid(Long geid) {
        this.geid = geid;
    }

    public Date getExDate() {
        return exDate;
    }

    public void setExDate(Date exDate) {
        this.exDate = exDate;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public Long getShuang11Point() {
        return shuang11Point;
    }

    public void setShuang11Point(Long shuang11Point) {
        this.shuang11Point = shuang11Point;
    }

    public String getShuang11Pic() {
        return shuang11Pic;
    }

    public void setShuang11Pic(String shuang11Pic) {
        this.shuang11Pic = shuang11Pic;
    }

    public int getWeixinExclusive() {
        return weixinExclusive;
    }

    public void setWeixinExclusive(int weixinExclusive) {
        this.weixinExclusive = weixinExclusive;
    }

    public boolean isNews() {
        return isNews;
    }

    public void setNews(boolean news) {
        isNews = news;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public int getReserveType() {
        return reserveType;
    }

    public void setReserveType(int reserveType) {
        this.reserveType = reserveType;
    }

    public Date getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(Date exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public int getHotActivity() {
        return hotActivity;
    }

    public void setHotActivity(int hotActivity) {
        this.hotActivity = hotActivity;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getSeckillType() {
        return seckillType;
    }

    public void setSeckillType(int seckillType) {
        this.seckillType = seckillType;
    }

    public String getYktitle() {
        return yktitle;
    }

    public void setYktitle(String yktitle) {
        this.yktitle = yktitle;
    }

    public String getYkdesc() {
        return ykdesc;
    }

    public void setYkdesc(String ykdesc) {
        this.ykdesc = ykdesc;
    }

    public String getYkpic() {
        return ykpic;
    }

    public void setYkpic(String ykpic) {
        this.ykpic = ykpic;
    }

    public String getSupscript() {
        return supscript;
    }

    public void setSupscript(String supscript) {
        this.supscript = supscript;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
