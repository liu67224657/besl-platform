package com.enjoyf.platform.service.point;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.content.MobileExclusive;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.text.TextJsonItem;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 15-5-4
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
public class ActivityGoods implements Serializable {
    private long activityGoodsId; //主键ID
    private long gameDbId;//游戏资料库ID 与游戏资料库的表关联
    private String activitySubject;//活动的标题
    private String activityDesc;//活动的描述
    private List<TextJsonItem> textJsonItemsList;//活动的描述JSON格式 提供给客户端
    private String activityPicUrl;//活动的图片
    private ActivityType activityType;//活动类型 0=积分商城 1=礼包中心
    private MobileExclusive channelType = MobileExclusive.DEFAULT;
    private GoodsActionType goodsActionType = GoodsActionType.WWW;
    private Date startTime;//活动开始时间
    private Date endTime;//活动结束时间
    private String subDesc;//小标题 描述
    private long passiveShareId; //领取后分享的ID
    private String firstLetter;  //首字母
    //TODO  displayType 写个类吧
    private ChooseType hotActivity;//是否加入热门礼包列表 0=不是 1=是  2=特殊活动
    private int reserveType;//预订状态 0=可兑换 1=可预订
    private GoodsType activitygoodsType;   //商品分类 0=实物商品 1=虚拟商品
    private int goodsAmount;//	礼包的总数
    private int goodsResetAmount;//已有数量
    private int goodsPoint;//需要的积分
    private ConsumeTimesType timeType;  //领取类型 1=永久一次 2=一天一次 3=不受限制
    private ConsumeTimesType taoTimesType = ConsumeTimesType.MANYTIMESADAY;
    private int displayType;//显示的类型（用移位来弄）:0_无 1_新品 2_热门
    private int displayOrder;      //时间排序
    private String bgPic;//背景图片
    private ActStatus actStatus; //
    private String createUserId;
    private Date createTime;
    private String createIp;
    private AppPlatform platform;  //平台
    private ChooseType seckilltype; //是否加入秒杀  0=不加入 1=加入
    private SecKill secKill;//秒杀字段


    public long getActivityGoodsId() {
        return activityGoodsId;
    }

    public void setActivityGoodsId(long activityGoodsId) {
        this.activityGoodsId = activityGoodsId;
    }

    public long getGameDbId() {
        return gameDbId;
    }

    public void setGameDbId(long gameDbId) {
        this.gameDbId = gameDbId;
    }

    public String getActivitySubject() {
        return activitySubject;
    }

    public void setActivitySubject(String activitySubject) {
        this.activitySubject = activitySubject;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public List<TextJsonItem> getTextJsonItemsList() {
        return textJsonItemsList;
    }

    public void setTextJsonItemsList(List<TextJsonItem> textJsonItemsList) {
        this.textJsonItemsList = textJsonItemsList;
    }

    public String getActivityPicUrl() {
        return activityPicUrl;
    }

    public void setActivityPicUrl(String activityPicUrl) {
        this.activityPicUrl = activityPicUrl;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public MobileExclusive getChannelType() {
        return channelType;
    }

    public void setChannelType(MobileExclusive channelType) {
        this.channelType = channelType;
    }

    public GoodsActionType getGoodsActionType() {
        return goodsActionType;
    }

    public void setGoodsActionType(GoodsActionType goodsActionType) {
        this.goodsActionType = goodsActionType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSubDesc() {
        return subDesc;
    }

    public void setSubDesc(String subDesc) {
        this.subDesc = subDesc;
    }

    public long getPassiveShareId() {
        return passiveShareId;
    }

    public void setPassiveShareId(long passiveShareId) {
        this.passiveShareId = passiveShareId;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }


    public int getReserveType() {
        return reserveType;
    }

    public void setReserveType(int reserveType) {
        this.reserveType = reserveType;
    }

    public GoodsType getActivitygoodsType() {
        return activitygoodsType;
    }

    public void setActivitygoodsType(GoodsType activitygoodsType) {
        this.activitygoodsType = activitygoodsType;
    }

    public int getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(int goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public int getGoodsResetAmount() {
        return goodsResetAmount;
    }

    public void setGoodsResetAmount(int goodsResetAmount) {
        this.goodsResetAmount = goodsResetAmount;
    }

    public int getGoodsPoint() {
        return goodsPoint;
    }

    public void setGoodsPoint(int goodsPoint) {
        this.goodsPoint = goodsPoint;
    }

    public ConsumeTimesType getTimeType() {
        return timeType;
    }

    public void setTimeType(ConsumeTimesType timeType) {
        this.timeType = timeType;
    }

    public ConsumeTimesType getTaoTimesType() {
        return taoTimesType;
    }

    public void setTaoTimesType(ConsumeTimesType taoTimesType) {
        this.taoTimesType = taoTimesType;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public ActStatus getActStatus() {
        return actStatus;
    }

    public void setActStatus(ActStatus actStatus) {
        this.actStatus = actStatus;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getBgPic() {
        return bgPic;
    }

    public void setBgPic(String bgPic) {
        this.bgPic = bgPic;
    }

    public AppPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(AppPlatform platform) {
        this.platform = platform;
    }

    public SecKill getSecKill() {
        return secKill;
    }

    public void setSecKill(SecKill secKill) {
        this.secKill = secKill;
    }

    public ChooseType getHotActivity() {
        return hotActivity;
    }

    public void setHotActivity(ChooseType hotActivity) {
        this.hotActivity = hotActivity;
    }

    public ChooseType getSeckilltype() {
        return seckilltype;
    }

    public void setSeckilltype(ChooseType seckilltype) {
        this.seckilltype = seckilltype;
    }

    @Override
    public String toString() {
        ActivityGoods activityGoods = new ActivityGoods();
        activityGoods.setStartTime(startTime);
        activityGoods.setActivityDesc(activityDesc);
        activityGoods.setActivityGoodsId(activityGoodsId);
        activityGoods.setActivitygoodsType(activitygoodsType);
        activityGoods.setActivityPicUrl(activityPicUrl);
        activityGoods.setActivitySubject(activitySubject);
        activityGoods.setActivityType(activityType);
        activityGoods.setActStatus(actStatus);
        activityGoods.setBgPic(bgPic);
        activityGoods.setChannelType(channelType);
        activityGoods.setCreateIp(createIp);
        activityGoods.setCreateTime(createTime);
        activityGoods.setCreateUserId(createUserId);
        activityGoods.setDisplayOrder(displayOrder);
        activityGoods.setDisplayType(displayType);
        activityGoods.setEndTime(endTime);
        activityGoods.setFirstLetter(firstLetter);
        activityGoods.setGameDbId(gameDbId);
        activityGoods.setGoodsActionType(goodsActionType);
        activityGoods.setGoodsAmount(goodsAmount);
        activityGoods.setGoodsPoint(goodsPoint);
        activityGoods.setGoodsResetAmount(goodsResetAmount);
        activityGoods.setHotActivity(hotActivity);
        activityGoods.setPassiveShareId(passiveShareId);
        activityGoods.setPlatform(platform);
        activityGoods.setReserveType(reserveType);
        activityGoods.setTimeType(timeType);
        activityGoods.setTextJsonItemsList(textJsonItemsList);
        activityGoods.setTaoTimesType(taoTimesType);
        activityGoods.setSubDesc(subDesc);
        activityGoods.setSeckilltype(seckilltype);
        activityGoods.setSecKill(secKill);
        return ReflectUtil.fieldsToString(activityGoods);
    }
}
