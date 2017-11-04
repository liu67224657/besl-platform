package com.enjoyf.platform.util.umeng;

import java.util.Arrays;
import java.util.HashSet;

import com.enjoyf.platform.util.StringUtil;
import org.json.JSONObject;

public abstract class AndroidNotification extends UmengNotification {

    private static final int PAYLOAD_BYTE_LENGTH = 750;
    // Keys can be set in the payload level
    protected static final HashSet<String> PAYLOAD_KEYS = new HashSet<String>(Arrays.asList(new String[]{
            "display_type"}));

    // Keys can be set in the body level
    protected static final HashSet<String> BODY_KEYS = new HashSet<String>(Arrays.asList(new String[]{
            "ticker", "title", "text", "builder_id", "icon", "largeIcon", "img", "play_vibrate", "play_lights", "play_sound",
            "sound", "after_open", "url", "activity", "custom"}));

    // Set key/value in the rootJson, for the keys can be set please see ROOT_KEYS, PAYLOAD_KEYS,
    // BODY_KEYS and POLICY_KEYS.
    @Override
    public boolean setPredefinedKeyValue(String key, Object value) throws Exception {
        if (ROOT_KEYS.contains(key)) {
            // This key should be in the root level
            rootJson.put(key, value);
        } else if (PAYLOAD_KEYS.contains(key)) {
            // This key should be in the payload level
            JSONObject payloadJson = null;
            if (rootJson.has("payload")) {
                payloadJson = rootJson.getJSONObject("payload");
            } else {
                payloadJson = new JSONObject();
                rootJson.put("payload", payloadJson);
            }
            payloadJson.put(key, value);
        } else if (BODY_KEYS.contains(key)) {
            // This key should be in the body level
            JSONObject bodyJson = null;
            JSONObject payloadJson = null;
            // 'body' is under 'payload', so build a payload if it doesn't exist
            if (rootJson.has("payload")) {
                payloadJson = rootJson.getJSONObject("payload");
            } else {
                payloadJson = new JSONObject();
                rootJson.put("payload", payloadJson);
            }
            // Get body JSONObject, generate one if not existed
            if (payloadJson.has("body")) {
                bodyJson = payloadJson.getJSONObject("body");
            } else {
                bodyJson = new JSONObject();
                payloadJson.put("body", bodyJson);
            }
            bodyJson.put(key, value);
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
            if (key == "payload" || key == "body" || key == "policy" || key == "extra") {
                throw new Exception("You don't need to set value for " + key + " , just set values for the sub keys in it.");
            } else {
                throw new Exception("Unknown key: " + key);
            }
        }
        return true;
    }

    // Set extra key/value for Android notification
    public boolean setExtraField(String key, String value) throws Exception {
        JSONObject extraJson = null;
        if (rootJson.has("extra")) {
            extraJson = rootJson.getJSONObject("extra");
        } else {
            extraJson = new JSONObject();
            rootJson.put("extra", extraJson);
        }
        extraJson.put(key, value);
        return true;
    }

    public boolean checkPayload(String checkStr) throws Exception {
        JSONObject payloadJson = rootJson.getJSONObject("payload");
        JSONObject bodyJson = payloadJson.getJSONObject("body");
        String text = String.valueOf(bodyJson.get("text"));
        String title = String.valueOf(bodyJson.get("title"));
        String ticker = String.valueOf(bodyJson.get("ticker"));
        //
        if (!StringUtil.isEmpty(text) && !StringUtil.isEmpty(title) && !StringUtil.isEmpty(ticker)) {
            byte[] c = checkStr.getBytes("UTF-8");
            byte[] tx = text.getBytes("UTF-8");
            byte[] ti = title.getBytes("UTF-8");
            byte[] tk = ticker.getBytes("UTF-8");

            if (c.length + tx.length + ti.length + tk.length > PAYLOAD_BYTE_LENGTH) {
                String subText = new String(tx, 0, tx.length - (c.length + tx.length + ti.length + tk.length - PAYLOAD_BYTE_LENGTH) - 1, "UTF-8");
                int length = subText.length();
                if (subText.charAt(subText.length() - 1) != text.charAt(subText.length() - 1)) {
                    subText = subText.substring(0, length - 1);
                }
                if (subText.length() > 4) {
                    subText = subText.substring(0, subText.length() - 1) + "...";
                }
                rootJson.getJSONObject("payload").getJSONObject("body").put("text", subText);
            }
        }
        return true;
    }

}
