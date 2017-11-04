package com.enjoyf.platform.util.umeng;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;

public abstract class IOSNotification extends UmengNotification {

    private static final int PAYLOAD_BYTE_LENGTH = 1000;

    // Keys can be set in the aps level
    protected static final HashSet<String> APS_KEYS = new HashSet<String>(Arrays.asList(new String[]{
            "alert", "badge", "sound", "content-available"
    }));

    protected static final HashSet<String> CUSTOM_KEYS = new HashSet<String>(Arrays.asList(new String[]{
            "msgtype", "info", "option", "jt", "ji"
    }));

    @Override
    public boolean setPredefinedKeyValue(String key, Object value) throws Exception {
        if (ROOT_KEYS.contains(key)) {
            // This key should be in the root level
            rootJson.put(key, value);
        } else if (APS_KEYS.contains(key)) {
            // This key should be in the aps level
            JSONObject apsJson = null;
            JSONObject payloadJson = null;
            if (rootJson.has("payload")) {
                payloadJson = rootJson.getJSONObject("payload");
            } else {
                payloadJson = new JSONObject();
                rootJson.put("payload", payloadJson);
            }
            if (payloadJson.has("aps")) {
                apsJson = payloadJson.getJSONObject("aps");
            } else {
                apsJson = new JSONObject();
                payloadJson.put("aps", apsJson);
            }
            apsJson.put(key, value);
        } else if (CUSTOM_KEYS.contains(key)) {
            JSONObject payloadJson = null;
            if (rootJson.has("payload")) {
                payloadJson = rootJson.getJSONObject("payload");
            } else {
                payloadJson = new JSONObject();
                rootJson.put("payload", payloadJson);
            }
            payloadJson.put(key, value);
        } else if (POLICY_KEYS.contains(key)) {
            // This key should be in the body level
            JSONObject policyJson = null;
            if (rootJson.has("policy")) {
                policyJson = rootJson.getJSONObject("policy");
            } else {
                policyJson = new JSONObject();
                rootJson.put("policy", policyJson);
            }
            policyJson.put(key, value);
        } else {
            if (key == "payload" || key == "aps" || key == "policy") {
                throw new Exception("You don't need to set value for " + key + " , just set values for the sub keys in it.");
            } else {
                throw new Exception("Unknownd key: " + key);
            }
        }

        return true;
    }

    // Set customized key/value for IOS notification
    public boolean setCustomizedField(String key, String value) throws Exception {
        rootJson.put(key, value);
        return true;
    }

    public boolean checkPayload(String checkStr, int len) throws Exception {
        JSONObject payloadJson = rootJson.getJSONObject("payload");
        JSONObject apsJson = payloadJson.getJSONObject("aps");
        String alert = String.valueOf(apsJson.get("alert"));
        if (alert != null) {
            byte[] c = checkStr.getBytes("UTF-8");
            byte[] a = alert.getBytes("UTF-8");

            if (c.length + a.length > PAYLOAD_BYTE_LENGTH - len) {
                String subAlert = new String(a, 0, PAYLOAD_BYTE_LENGTH - len - c.length, "UTF-8");
                int length = subAlert.length();
                if (subAlert.charAt(subAlert.length() - 1) != alert.charAt(subAlert.length() - 1)) {
                    subAlert = subAlert.substring(0, length - 1);
                }
                if (subAlert.length() > 4) {
                    subAlert = subAlert.substring(0, subAlert.length() - 1) + "...";
                }
                rootJson.getJSONObject("payload").getJSONObject("aps").put("alert", subAlert);
            }
        }
        return true;
    }

}