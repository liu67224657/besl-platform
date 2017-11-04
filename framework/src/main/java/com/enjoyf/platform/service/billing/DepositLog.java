package com.enjoyf.platform.service.billing;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericLiu
 * Date: 12-1-6
 * Time: 下午6:04
 * To change this template use File | Settings | File Templates.
 */
public class DepositLog implements Serializable {
    private String logId;
    private String appChannel;
    private String appKey;

    private String orderId;
    private String profileId;

    private String zoneKey;
    private DepositChannel channel;
    private AppPlatform appPlatform;
    private int amount;
    private String currency = "RMB";
    private String paytype;
    private IntValidStatus status;
    private String feeType;
    private String transType;
    private String transId;
    private Date depositTime;
    private String depositIp;
    private String info;
    private String thirdOrderid;
    private String productId;
    private String producetName;
    private String gameUid;//todo 暂时无用
    private int syncTimes;

    private IntValidStatus syncStatus = IntValidStatus.VALIDING;// VALIDING同步中 VALID同步成功 UNVALID同步失败
    private String errorMsg;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getZoneKey() {
        return zoneKey;
    }

    public void setZoneKey(String zoneKey) {
        this.zoneKey = zoneKey;
    }

    public DepositChannel getChannel() {
        return channel;
    }

    public void setChannel(DepositChannel channel) {
        this.channel = channel;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public IntValidStatus getStatus() {
        return status;
    }

    public void setStatus(IntValidStatus status) {
        this.status = status;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public Date getDepositTime() {
        return depositTime;
    }

    public void setDepositTime(Date depositTime) {
        this.depositTime = depositTime;
    }

    public String getDepositIp() {
        return depositIp;
    }

    public void setDepositIp(String depositIp) {
        this.depositIp = depositIp;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAppChannel() {
        return appChannel;
    }

    public void setAppChannel(String appChannel) {
        this.appChannel = appChannel;
    }

    public AppPlatform getAppPlatform() {
        return appPlatform;
    }

    public void setAppPlatform(AppPlatform appPlatform) {
        this.appPlatform = appPlatform;
    }

    public IntValidStatus getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(IntValidStatus syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProducetName() {
        return producetName;
    }

    public void setProducetName(String producetName) {
        this.producetName = producetName;
    }

    public String getThirdOrderid() {
        return thirdOrderid;
    }

    public void setThirdOrderid(String thirdOrderid) {
        this.thirdOrderid = thirdOrderid;
    }

    public String getGameUid() {
        return gameUid;
    }

    public void setGameUid(String gameUid) {
        this.gameUid = gameUid;
    }

    public int getSyncTimes() {
        return syncTimes;
    }

    public void setSyncTimes(int syncTimes) {
        this.syncTimes = syncTimes;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
