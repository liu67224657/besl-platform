package com.enjoyf.platform.service.billing;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.thirddeposit.CpResult;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import net.sf.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by ericliu on 15/12/15.
 */
public class DepositResultSender {
    private static HttpClientManager httpClientManager = new HttpClientManager();

    public static CpResult sendToGameServer(String callBack, DepositLog log) {
        CpResult cpResult = new CpResult();
        HttpResult httpResult = null;
        if (!IntValidStatus.VALID.equals(log.getStatus())) {
            httpResult = httpClientManager.post(callBack, new HttpParameter[]{
                    new HttpParameter("result", 0),
                    new HttpParameter("msg", "system.error"),
                    new HttpParameter("orderid", log.getThirdOrderid()),
                    new HttpParameter("logid", log.getLogId())
            }, null);
        } else {
            httpResult = httpClientManager.post(callBack, new HttpParameter[]{
                    new HttpParameter("result", 1),
                    new HttpParameter("msg", "success"),
                    new HttpParameter("logid", log.getLogId()),
                    new HttpParameter("profileid", log.getProfileId()),
                    new HttpParameter("zonekey", log.getZoneKey()),
                    new HttpParameter("appkey", log.getAppKey()),
                    new HttpParameter("productid", log.getProductId()),
                    new HttpParameter("orderid", log.getThirdOrderid()),
                    new HttpParameter("money", changeF2Y(String.valueOf(log.getAmount())))
            }, null);
        }
        if (httpResult == null) {
            cpResult.setSyncStatus(500);
            return cpResult;
        }

        if (httpResult.getReponseCode() != 200) {
            cpResult.setSyncStatus(httpResult.getReponseCode());
            return cpResult;
        }


        if (StringUtil.isEmpty(httpResult.getResult())) {
            cpResult.setSyncStatus(0);
            return cpResult;
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.fromObject(httpResult.getResult());
            cpResult.setErrormsg(httpResult.getResult());
            GAlerter.lan("get callbackcallback:" + callBack + ",result:" + httpResult.getResult() + ",log," + log.toString());
        } catch (Exception e) {
            GAlerter.lan(DepositResultSender.class.getName() + " deposit sync game error. result:" + httpResult, e);
        }

        if (jsonObject == null || !jsonObject.containsKey("rs")) {
            cpResult.setSyncStatus(0);
            return cpResult;
        }

        try {
            cpResult.setSyncStatus(jsonObject.getInt("rs"));
        } catch (Exception e) {
            GAlerter.lan(DepositResultSender.class.getName() + " deposit sync game error rs not int. result:" + httpResult, e);
        }
        return cpResult;
    }

    /**
     * 将分为单位的转换为元 （除100）
     */
    private static String changeF2Y(String amount) {
        return BigDecimal.valueOf(Double.valueOf(amount)).divide(new BigDecimal(100)).toString();
    }
}
