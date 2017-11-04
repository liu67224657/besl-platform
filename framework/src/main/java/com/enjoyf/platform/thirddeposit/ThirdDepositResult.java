package com.enjoyf.platform.thirddeposit;

import com.enjoyf.platform.service.billing.DepositLog;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/9/15
 * Description:
 */
public class ThirdDepositResult {
    public static final int SUCCESS = 1;
    public static final int REQUEST_ILLEGL = -1;//
    public static final int REQUEST_VERIFY_FAILED = -2;
    public static final int ORDER_FAILED = -3;

    private int result;
    private String msg;
    private DepositLog depositLog;

    public ThirdDepositResult() {
    }

    public ThirdDepositResult(int result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DepositLog getDepositLog() {
        return depositLog;
    }

    public void setDepositLog(DepositLog depositLog) {
        this.depositLog = depositLog;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
