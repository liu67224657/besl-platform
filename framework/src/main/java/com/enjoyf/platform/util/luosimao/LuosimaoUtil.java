package com.enjoyf.platform.util.luosimao;

import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.util.HttpClientManager;
import com.enjoyf.util.HttpParameter;
import com.enjoyf.util.HttpResult;

/**
 * Created by zhimingli on 2016/11/23 0023.
 */
public class LuosimaoUtil {

    private static HttpClientManager httpClient = new HttpClientManager();

    private static String API_KEY = "b835a45eb28ec46b8f79bb87eea9cb05";

    public static boolean verify(String response) {
        boolean bal = false;
        if (StringUtil.isEmpty(response)) {
            return false;
        }

        HttpResult httpResult = httpClient.post("https://captcha.luosimao.com/api/site_verify", new HttpParameter[]{
                new HttpParameter("api_key", API_KEY),
                new HttpParameter("response", response)
        }, null);

        GAlerter.lan("LuosimaoUtil--->" + httpResult.getReponseCode() + "=====" + httpResult.getResult());
        if (httpResult.getReponseCode() == 200) {
            if (httpResult.getResult().contains("success")) {
                bal = true;
            }
        }
        return bal;
    }


    public static void main(String[] args) {
        String str = "";
        boolean bval = verify(str);
        System.out.println(bval);
    }
}
