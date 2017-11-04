package com.enjoyf.platform.util.umeng;

import com.enjoyf.platform.util.umeng.android.*;
import com.enjoyf.platform.util.umeng.ios.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Demo {
    private String appkey = null;
    private String masterSecret = null;
    private String timestamp = null;
    private String validationToken = null;

    public Demo(String key, String secret) {
        try {
            appkey = key;
            masterSecret = secret;
            timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000));
            // Generate MD5 of appkey, masterSecret and timestamp as validation_token
            validationToken = DigestUtils.md5Hex(appkey.toLowerCase() + masterSecret.toLowerCase() + timestamp);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void sendAndroidBroadcast() throws Exception {
        AndroidBroadcast broadcast = new AndroidBroadcast();
        broadcast.setPredefinedKeyValue("appkey", this.appkey);
        broadcast.setPredefinedKeyValue("timestamp", this.timestamp);
        broadcast.setPredefinedKeyValue("validation_token", this.validationToken);
        broadcast.setPredefinedKeyValue("ticker", "Android broadcast ticker");
        broadcast.setPredefinedKeyValue("title", "中文的title");
        broadcast.setPredefinedKeyValue("text", "Android broadcast text");
        broadcast.setPredefinedKeyValue("after_open", "go_app");
        broadcast.setPredefinedKeyValue("display_type", "notification");
        // Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        broadcast.setPredefinedKeyValue("production_mode", "false");
        // Set customized fields
        broadcast.setExtraField("test", "helloworld");
        broadcast.send();
    }

    public void sendAndroidUnicast() throws Exception {
        AndroidUnicast unicast = new AndroidUnicast();
        unicast.setPredefinedKeyValue("appkey", this.appkey);
        unicast.setPredefinedKeyValue("timestamp", this.timestamp);
        unicast.setPredefinedKeyValue("validation_token", this.validationToken);
        //  Set your device token
        unicast.setPredefinedKeyValue("device_tokens", "12345678901234567890123456789012345678901234");
        unicast.setPredefinedKeyValue("ticker", "一二三四五六七八九十");
        unicast.setPredefinedKeyValue("title", "一二三四五六七八九十");
        unicast.setPredefinedKeyValue("text", "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十");
        unicast.setPredefinedKeyValue("after_open", "go_app");
        unicast.setPredefinedKeyValue("display_type", "notification");
        //  Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        unicast.setPredefinedKeyValue("production_mode", "false");
        // Set customized fields
        unicast.setExtraField("test", "helloworld");
        unicast.send();
    }

    public void sendAndroidGroupcast() throws Exception {
        AndroidGroupcast groupcast = new AndroidGroupcast();
        groupcast.setPredefinedKeyValue("appkey", this.appkey);
        groupcast.setPredefinedKeyValue("timestamp", this.timestamp);
        groupcast.setPredefinedKeyValue("validation_token", this.validationToken);
        /*
         *  Construct the filter condition:
		 *  "where": 
		 *	{
    	 *		"and": 
    	 *		[
      	 *			{"tag":"test"},
      	 *			{"tag":"Test"}
    	 *		]
		 *	}
		 */
        JSONObject filterJson = new JSONObject();
        JSONObject whereJson = new JSONObject();
        JSONArray tagArray = new JSONArray();
        JSONObject testTag = new JSONObject();
        JSONObject TestTag = new JSONObject();
        testTag.put("tag", "test");
        TestTag.put("tag", "Test");
        tagArray.put(testTag);
        tagArray.put(TestTag);
        whereJson.put("and", tagArray);
        filterJson.put("where", whereJson);
        System.out.println(filterJson.toString());

        groupcast.setPredefinedKeyValue("filter", filterJson);
        groupcast.setPredefinedKeyValue("ticker", "Android groupcast ticker");
        groupcast.setPredefinedKeyValue("title", "中文的title");
        groupcast.setPredefinedKeyValue("text", "Android groupcast text");
        groupcast.setPredefinedKeyValue("after_open", "go_app");
        groupcast.setPredefinedKeyValue("display_type", "notification");
        //  Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        groupcast.setPredefinedKeyValue("production_mode", "true");
        groupcast.send();
    }

    public void sendAndroidCustomizedcast() throws Exception {
        AndroidCustomizedcast customizedcast = new AndroidCustomizedcast();
        customizedcast.setPredefinedKeyValue("appkey", this.appkey);
        customizedcast.setPredefinedKeyValue("timestamp", this.timestamp);
        customizedcast.setPredefinedKeyValue("validation_token", this.validationToken);
        //  Set your alias here, and use comma to split them if there are multiple alias.
        // And if you have many alias, you can also upload a file containing these alias, then
        // use file_id to send customized notification.
        customizedcast.setPredefinedKeyValue("alias", "xx");
        customizedcast.setPredefinedKeyValue("ticker", "Android customizedcast ticker");
        customizedcast.setPredefinedKeyValue("title", "中文的title");
        customizedcast.setPredefinedKeyValue("text", "Android customizedcast text");
        customizedcast.setPredefinedKeyValue("after_open", "go_app");
        customizedcast.setPredefinedKeyValue("display_type", "notification");
        //  Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        customizedcast.setPredefinedKeyValue("production_mode", "true");
        customizedcast.send();
    }

    public void sendAndroidFilecast(String contents) throws Exception {
        AndroidFilecast filecast = new AndroidFilecast();
        filecast.setPredefinedKeyValue("appkey", this.appkey);
        filecast.setPredefinedKeyValue("timestamp", this.timestamp);
        filecast.setPredefinedKeyValue("validation_token", this.validationToken);
        //  upload your device tokens, and use '\n' to split them if there are multiple tokens
        filecast.uploadContents(contents);
        filecast.setPredefinedKeyValue("ticker", "冰冰冰冰冰冰冰冰");
        filecast.setPredefinedKeyValue("title", "冰冰冰冰冰冰冰冰");
        filecast.setPredefinedKeyValue("text", "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十" +
//                "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十" +
//                "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十" +
//                "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十" +
                "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十");
        filecast.setPredefinedKeyValue("after_open", "go_custom");
        filecast.setPredefinedKeyValue("custom", "{\"jt\":\"4\",\"ji\":\"http://t.cn/h5mwx\"}");
//        filecast.setExtraField("jt", "4");
//        filecast.setExtraField("ji", "http://www.baidu.com/");
        filecast.setPredefinedKeyValue("display_type", "notification");
        filecast.setPredefinedKeyValue("production_mode", "false");
//        filecast.checkPayload("{\"jt\":\"4\",\"ji\":\"http://t.cn/h5mwx\"}");
        filecast.send();
    }

    public void sendIOSBroadcast() throws Exception {
        IOSBroadcast broadcast = new IOSBroadcast();
        broadcast.setPredefinedKeyValue("appkey", this.appkey);
        broadcast.setPredefinedKeyValue("timestamp", this.timestamp);
        broadcast.setPredefinedKeyValue("validation_token", this.validationToken);
        broadcast.setPredefinedKeyValue("alert", "IOS 广播测试");
        broadcast.setPredefinedKeyValue("badge", 0);
        broadcast.setPredefinedKeyValue("sound", "chime");
        //  set 'production_mode' to 'true' if your app is under production mode
        broadcast.setPredefinedKeyValue("production_mode", "false");
        // Set customized fields
        broadcast.setCustomizedField("test", "helloworld");
        broadcast.send();
    }

    public void sendIOSUnicast() throws Exception {
        IOSUnicast unicast = new IOSUnicast();
        unicast.setPredefinedKeyValue("appkey", this.appkey);
        unicast.setPredefinedKeyValue("timestamp", this.timestamp);
        unicast.setPredefinedKeyValue("validation_token", this.validationToken);
        //  Set your device token
        unicast.setPredefinedKeyValue("device_tokens", "d0755283dd4fd9370a36beeb17500c2a0b0f592a9d37f7e1b9ed50e3a23b340d");
        unicast.setPredefinedKeyValue("alert", "IOS 单播测试");
        unicast.setPredefinedKeyValue("badge", 0);
        unicast.setPredefinedKeyValue("sound", "default");
        //  set 'production_mode' to 'true' if your app is under production mode
        unicast.setPredefinedKeyValue("production_mode", "false");
        // Set customized fields
        unicast.setCustomizedField("test", "helloworld");
        unicast.send();
    }

    public void sendIOSGroupcast() throws Exception {
        IOSGroupcast groupcast = new IOSGroupcast();
        groupcast.setPredefinedKeyValue("appkey", this.appkey);
        groupcast.setPredefinedKeyValue("timestamp", this.timestamp);
        groupcast.setPredefinedKeyValue("validation_token", this.validationToken);
		/*
		 *  Construct the filter condition:
		 *  "where": 
		 *	{
    	 *		"and": 
    	 *		[
      	 *			{"tag":"iostest"}
    	 *		]
		 *	}
		 */
        JSONObject filterJson = new JSONObject();
        JSONObject whereJson = new JSONObject();
        JSONArray tagArray = new JSONArray();
        JSONObject testTag = new JSONObject();
//		testTag.put("tag", "iostest");
        tagArray.put(testTag);
        whereJson.put("and", tagArray);
        filterJson.put("where", whereJson);
        System.out.println(filterJson.toString());

        // Set filter condition into rootJson
        groupcast.setPredefinedKeyValue("filter", filterJson);
        groupcast.setPredefinedKeyValue("alert", "IOS 组播测试");
        groupcast.setPredefinedKeyValue("badge", 0);
        groupcast.setPredefinedKeyValue("sound", "chime");
        //  set 'production_mode' to 'true' if your app is under production mode
        groupcast.setPredefinedKeyValue("production_mode", "false");
        groupcast.send();
    }

    public void sendIOSCustomizedcast() throws Exception {
        IOSCustomizedcast customizedcast = new IOSCustomizedcast();
        customizedcast.setPredefinedKeyValue("appkey", this.appkey);
        customizedcast.setPredefinedKeyValue("timestamp", this.timestamp);
        customizedcast.setPredefinedKeyValue("validation_token", this.validationToken);
        //  Set your alias here, and use comma to split them if there are multiple alias.
        // And if you have many alias, you can also upload a file containing these alias, then
        // use file_id to send customized notification.
        customizedcast.setPredefinedKeyValue("alias", "xx");
        customizedcast.setPredefinedKeyValue("alert", "IOS 个性化测试");
        customizedcast.setPredefinedKeyValue("badge", 0);
        customizedcast.setPredefinedKeyValue("sound", "chime");
        //  set 'production_mode' to 'true' if your app is under production mode
        customizedcast.setPredefinedKeyValue("production_mode", "false");
        customizedcast.send();
    }

    public void sendIOSFilecast(String contents) throws Exception {
        IOSFilecast filecast = new IOSFilecast();
        filecast.setPredefinedKeyValue("appkey", this.appkey);
        filecast.setPredefinedKeyValue("timestamp", this.timestamp);
        filecast.setPredefinedKeyValue("validation_token", this.validationToken);
        //  upload your device tokens, and use '\n' to split them if there are multiple tokens
        filecast.uploadContents(contents);
        filecast.setPredefinedKeyValue("alert", "一二三四五六七八九十一二三四五六七八九十一二三四五");
        filecast.setPredefinedKeyValue("badge", 1);
        filecast.setPredefinedKeyValue("sound", "default");

        filecast.setPredefinedKeyValue("msgtype", 6);
        filecast.setPredefinedKeyValue("info", "http://www.baidu.com/");

        filecast.setPredefinedKeyValue("jt", 1);
        filecast.setPredefinedKeyValue("ji", "http://t.cn/RhM4PeN");

//        filecast.setPredefinedKeyValue("option", "");
//        filecast.setPredefinedKeyValue("option", "{\"list\":[{\"type\":1,\"info\":\"http://marticle.joyme.com/marticle/syhbao/pingguoduan/201409/0550552.html\"}]}");
        //  set 'production_mode' to 'true' if your app is under production mode
        filecast.setPredefinedKeyValue("production_mode", "false");
        filecast.checkPayload("default1jtji" + 1 + "http://t.cn/RhM4PeN", 12);
        filecast.send();
    }

    public static void main(String[] args) {
        //  set your appkey and master secret here
        //咔哒
//        Demo demo = new Demo("5382fd0956240b9ba42517e5", "haooaw0qlbosrsenu421irv2xj1tsn16");
        //画报 ios
//        Demo demo = new Demo("5382de4756240bba1f120d1c", "rzlg7lrxjsvplbxjvn6mbyf3w8d2vlcs");

        //画报 android
        Demo demo = new Demo("53687e3956240b692500cb0a", "1550r1ckoj61fojy89d7iju3lzx6bekp");
        try {
            //  upload your device tokens, and use '\n' to split them if there are multiple tokens
//            String contents = "fe9b7aa19b97621b4829172e39942e4a0b0ac7c71344cf9debc0db4f544fcf98" + "\n";
//            demo.sendIOSFilecast(contents);

            String contents = "AizNqvlETstNmKqI7DFHUQlbZXVd0CAi57Fm5hljjuuA" + "\n";
            demo.sendAndroidFilecast(contents);
//            demo.sendAndroidUnicast();

            System.exit(0);
			/*  these methods are all available, just fill in some fields and do the test
			 * demo.sendAndroidBroadcast();
			 * demo.sendAndroidGroupcast();
			 * demo.sendAndroidCustomizedcast();
			 * demo.sendAndroidFilecast();
			 * 
			 * demo.sendIOSBroadcast();
			 * demo.sendIOSUnicast();
			 * demo.sendIOSGroupcast();
			 * demo.sendIOSCustomizedcast();
			 * demo.sendIOSFilecast();
			 */
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
