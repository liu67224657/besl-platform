package com.enjoyf.webapps.joyme.weblogic.usercenter;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.util.HttpClientManager;
import com.enjoyf.util.HttpParameter;
import com.enjoyf.util.HttpResult;
import net.sf.json.JSONObject;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:2017/1/3
 */
public class UserCenterWebUtil {
    public static String getEditNum(Long uid) {
        HttpClientManager httpClientManager = new HttpClientManager();
        HttpResult httpResult = httpClientManager.post("http://wiki." + WebappConfig.get().DOMAIN + "/home/joyme_api.php", new HttpParameter[]{
                new HttpParameter("action", "userwikiinfo"),
                new HttpParameter("userid", uid)
        }, null);
        if (httpResult.getReponseCode() != 200) {
            return "0";
        }

        String result = "";
        try {
            result = httpResult.getResult();

            JSONObject jsonObject = JSONObject.fromObject(result);
            if (!jsonObject.containsKey("msg")) {
                return "0";
            }
            jsonObject = jsonObject.getJSONObject("msg");
            if (!jsonObject.containsKey("userprofiles")) {
                return "0";
            }
            return jsonObject.getJSONObject("userprofiles").get("total_edit_count").toString();
        } catch (Exception e) {
            GAlerter.lab("get edit num occured error. result:" + result + " e:", e);
        }
        return "0";
    }

}
