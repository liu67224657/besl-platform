/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.log;

/**
 * @Auther: <a mailto="ericliu@fivewh.com">Ericliu</a>
 * Create time: 11-9-6 下午1:47
 * Description:
 */
public class PageViewLog {

    private String locationUrl; //URL
    private String channel;       //渠道
    private String refer;       //来源


    private String sessionId;   //sessionid
    private String globalId;    //生成一个全局的id
    private String uno;         //uno

    private String userAgent;   //浏览器代理

    private String ip;     //IP地址

    public String getLocationUrl() {
        return locationUrl;
    }

    public void setLocationUrl(String locationUrl) {
        this.locationUrl = locationUrl;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getGlobalId() {
        return globalId;
    }

    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    //
    @Override
    public String toString() {
        return "["+ ip +"] ["+locationUrl+"] ["+ channel +"] ["+refer+"] ["+sessionId+"] ["+globalId+"] ["+uno+"] ["+userAgent+"]";
    }

}
