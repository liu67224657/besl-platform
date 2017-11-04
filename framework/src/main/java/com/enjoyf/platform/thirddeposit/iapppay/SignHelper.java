package com.enjoyf.platform.thirddeposit.iapppay;

import java.util.HashMap;
import java.util.Map;

public class SignHelper {
    // 字符编码格式 ，目前支持  utf-8
    public static String input_charset = "utf-8";

    public static boolean verify(String content, String sign, String pubKey) {
        // 目前版本，只支持RSA
        return RSA.verify(content, sign, pubKey, input_charset);
    }

    public static String sign(String content, String privateKey) {
        return RSA.sign(content, privateKey, input_charset);
    }

    public static String md5(String s) {
        return RSA.md5s(s);
    }

    public static Map<String, String> getSignParams(String respData) {
        //开始分割参数
        String transdata = "transdata"; // "{\"loginname\":\"18701637882\",\"userid\":\"14382295\"}";
        String sign = "sign"; // "HU6L6dZNR0PJEgsINI5Dlt2L2WfCsN8WDAUP+i/mLNIIwMVCHBBB6GKSrLvz10B5w5LGnX0PQf74oJx8O7JBOMJyQ7oQWoIs4NcpRi73BSxqdnt8XUTIBjfg33sfuGCCQO6GEW6gFHnocsXzNq8MIWk9mvCOFRL3pp/GmKdbbhQ=";
        String signtype = "signtype"; // "RSA";

        Map<String, String> reslutMap = new HashMap<String, String>();

        String[] dataArray = respData.split("&");

        for (String s : dataArray) {

            if (s.startsWith(transdata)) {
                reslutMap.put(transdata, s.substring(s.indexOf("=") + 1, s.length()));
            } else if (s.startsWith(signtype)) {
                reslutMap.put(signtype, s.substring(s.indexOf("=") + 1, s.length()));
            } else if (s.startsWith(sign)) {
                reslutMap.put(sign, s.substring(s.indexOf("=") + 1, s.length()));
            }
        }
        return reslutMap;
    }
}
