package com.enjoyf.platform.service.log;

import com.enjoyf.platform.util.CollectionUtil;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-9-30
 * Time: 上午9:56
 * To change this template use File | Settings | File Templates.
 */
public class MobileAccessLog {

    private String url;
    private String ip;
    private String deviceid;
    private String channel;
    private String platform;
    private String appkey;
    private String version;
    private String uno;
    private Map<String, String[]> params;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Map<String, String[]> getParams() {
        return params;
    }

    public void setParams(Map<String, String[]> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (!CollectionUtil.isEmpty(params)) {
            Map<String, String[]> map = params;
            for (Map.Entry<String, String[]> entry : map.entrySet()) {
                sb.append(entry.getKey()).append(":");

                for (int i = 0; i < entry.getValue().length; i++) {
                    sb.append(entry.getValue()[i]);
                    if (i < entry.getValue().length - 1) {
                        sb.append("&");
                    }
                }
                sb.append(" ");
            }
        }
        return "[" + ip + "] [" + url + "] [" + sb.toString() + "] [" + deviceid + "] [" + channel + "] [" + platform + "] [" + appkey + "] [" + version + "] [" + uno + "]";
    }
}
