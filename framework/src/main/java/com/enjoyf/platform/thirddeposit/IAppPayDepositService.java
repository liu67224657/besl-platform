package com.enjoyf.platform.thirddeposit;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.billing.BillingUtil;
import com.enjoyf.platform.service.billing.DepositChannel;
import com.enjoyf.platform.service.billing.DepositLog;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.thirddeposit.iapppay.AndroidIAppPaySDKConfig;
import com.enjoyf.platform.thirddeposit.iapppay.IOSIAppPaySDKConfig;
import com.enjoyf.platform.thirddeposit.iapppay.SignHelper;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.json.JoymeJsonObject;
import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/9/15
 * Description:
 */
public class IAppPayDepositService {

    private Map<AppPlatform, String> configMap = new HashMap<AppPlatform, String>();

    private static IAppPayDepositService instance = null;

    public static IAppPayDepositService get() {
        if (instance == null) {
            synchronized (IAppPayDepositService.class) {
                if (instance == null) {
                    instance = new IAppPayDepositService();
                }
            }
        }
        return instance;
    }

    private IAppPayDepositService() {
        configMap.put(AppPlatform.ANDROID, AndroidIAppPaySDKConfig.PLATP_KEY);
        configMap.put(AppPlatform.IOS, IOSIAppPaySDKConfig.PLATP_KEY);
    }


    public ThirdDepositResult getDepostiLogByRequest(String transdata, String sign, String signType, AppPlatform appPlatform) {
        String publicKey = configMap.get(appPlatform);
        //http://www.iapppay.com/g-introduction.html
        //数据传输的时候参数值需要进行urlencode，urlencode规则使用application/x-www-form-urlencoded方式，即空格” ”
        // 会被编码成加号”+”。进行urlencode编码时，中文字符采用UTF-8编码。

        sign = sign.replaceAll(" ", "+");
        //todo usercenter test
        if (!SignHelper.verify(transdata, sign, publicKey)) {
            return new ThirdDepositResult(ThirdDepositResult.REQUEST_VERIFY_FAILED, "request.verify.failed");
        }

        JSONObject jsonObject = JSONObject.fromObject(transdata);

        JoymeJsonObject appObj = new JoymeJsonObject(jsonObject);

        DepositLog depositLog = new DepositLog();
        depositLog.setTransId(appObj.getString("transid"));

        String appuserid = appObj.getString("appuserid");
        String userids[] = appuserid.split("#");
        depositLog.setProfileId(userids[0]);
        if (userids.length > 1 && !userids[1].equals("(null)")) {
            depositLog.setZoneKey(userids[1]);
        } else {
            depositLog.setZoneKey("");
        }
        depositLog.setOrderId(appObj.getString("cporderid"));
        depositLog.setCurrency(appObj.getString("RMB"));
        depositLog.setFeeType(appObj.getString("feetype"));
        Double money = appObj.getDouble("money");
        if (money != null) {
            depositLog.setAmount((int) (money * 100));
        }
        //"paytype":401,"result":0,"transid":"32021509151442586763","transtime":"2015-09-15 14:43:08","transtype":0,"waresid":1
        depositLog.setPaytype(appObj.getString("paytype"));
        depositLog.setStatus(appObj.getInt("result") == 0 ? IntValidStatus.VALID : IntValidStatus.UNVALID);
        depositLog.setTransId(appObj.getString("transid"));
        depositLog.setAppPlatform(appPlatform);
        depositLog.setChannel(DepositChannel.IAPPPAY);

//        appkey#channel#thirdorderid#productid#productname#depositip#adcode
        String info = appObj.getString("cpprivate");
        String infos[] = info.split("#");
        depositLog.setAppKey(infos[0]);
        if (infos.length > 1) {
            depositLog.setAppChannel(infos[1]);
        }
        if (infos.length > 2) {
            depositLog.setThirdOrderid(infos[2]);
        }
        if (infos.length > 3) {
            depositLog.setProductId(infos[3]);
        }
        if (infos.length > 4) {
            depositLog.setProducetName(infos[4]);
        }

        if (infos.length > 5) {
            depositLog.setDepositIp(infos[5]);
        }

        depositLog.setInfo(info);

        depositLog.setLogId(BillingUtil.getLogId(depositLog.getOrderId(), DepositChannel.IAPPPAY));

        String transTime = appObj.getString("transtime");
        Date date = null;
        try {
            date = DateUtil.formatStringToDate(transTime, DateUtil.DEFAULT_DATE_FORMAT2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        depositLog.setDepositTime(date);
        depositLog.setTransType(appObj.getString("transtype"));

        ThirdDepositResult result = new ThirdDepositResult(ThirdDepositResult.SUCCESS, "success");
        result.setDepositLog(depositLog);

        return result;
    }


    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = "%7B%22appid%22%3A%223002808173%22%2C%22appuserid%22%3A%223cd331c86d034771f9e298d12ad25e97%231001%22%2C%22cporderid%22%3A%221446820387512%22%2C%22cpprivate%22%3A%220j3C8B0O17WpkxJbmfDPje%23joyme%231001-201511-144682035852466%23172.16.81.33%22%2C%22currency%22%3A%22RMB%22%2C%22feetype%22%3A0%2C%22money%22%3A0.01%2C%22paytype%22%3A401%2C%22result%22%3A0%2C%22transid%22%3A%2232071511062233088620%22%2C%22transtime%22%3A%222015-11-06+22%3A33%3A19%22%2C%22transtype%22%3A0%2C%22waresid%22%3A1%7D";
        s = URLDecoder.decode(s, "utf-8");
        System.out.println(IAppPayDepositService.get().getDepostiLogByRequest(s, "sign", "RSA", AppPlatform.ANDROID).getDepositLog().getInfo());

    }

}
