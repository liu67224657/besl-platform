package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-1-12
 * Time: 上午10:37
 * To change this template use File | Settings | File Templates.
 */
public class BillingEncourageEvent extends SystemEvent {
    //
    private String ownUno;


    private String srcId;
    private String depositUno;
    private String depositIp;

    public BillingEncourageEvent() {
        super(SystemEventType.BILLING_ENCOURAGE_EVENT);
    }

    public String getOwnUno() {
        return ownUno;
    }

    public void setOwnUno(String ownUno) {
        this.ownUno = ownUno;
    }


    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public String getDepositUno() {
        return depositUno;
    }

    public void setDepositUno(String depositUno) {
        this.depositUno = depositUno;
    }

    public String getDepositIp() {
        return depositIp;
    }

    public void setDepositIp(String depositIp) {
        this.depositIp = depositIp;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return ownUno.hashCode();
    }
}
