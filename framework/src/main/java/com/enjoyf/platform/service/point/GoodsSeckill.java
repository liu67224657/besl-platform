package com.enjoyf.platform.service.point;

import com.enjoyf.platform.service.IntRemoveStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhitaoshi on 2015/7/23.
 */
public class GoodsSeckill implements Serializable{

    private long seckillId;
    private String goodsId;                //秒杀商品ID

    private String seckillTitle;          //标题
    private String seckillDesc;           //描述
    private String seckillPic;            //图片
    private Date startTime;               //开始时间
    private Date endTime;                 //结束时间
    private int seckillTotal;           //参与秒杀的库存总数
    private int seckillSum;                 //秒杀数
    private SeckillTips seckillTips;     //提示语

    private GoodsActionType goodsActionType;  //

    private IntRemoveStatus removeStatus;

    private String createUser;          //创建人
    private String modifyUser;          //修改人
    private Date createDate;            //创建时间
    private Date modifyDate;            //修改时间

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getSeckillTitle() {
        return seckillTitle;
    }

    public void setSeckillTitle(String seckillTitle) {
        this.seckillTitle = seckillTitle;
    }

    public String getSeckillDesc() {
        return seckillDesc;
    }

    public void setSeckillDesc(String seckillDesc) {
        this.seckillDesc = seckillDesc;
    }

    public String getSeckillPic() {
        return seckillPic;
    }

    public void setSeckillPic(String seckillPic) {
        this.seckillPic = seckillPic;
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

    public int getSeckillTotal() {
        return seckillTotal;
    }

    public void setSeckillTotal(int seckillTotal) {
        this.seckillTotal = seckillTotal;
    }

    public SeckillTips getSeckillTips() {
        return seckillTips;
    }

    public void setSeckillTips(SeckillTips seckillTips) {
        this.seckillTips = seckillTips;
    }

    public GoodsActionType getGoodsActionType() {
        return goodsActionType;
    }

    public void setGoodsActionType(GoodsActionType goodsActionType) {
        this.goodsActionType = goodsActionType;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public IntRemoveStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(IntRemoveStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public int getSeckillSum() {
        return seckillSum;
    }

    public void setSeckillSum(int seckillSum) {
        this.seckillSum = seckillSum;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
