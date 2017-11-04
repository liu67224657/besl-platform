package com.enjoyf.platform.service.oauth;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-1-16
 * Time: 上午10:09
 */
public class OAuthInfoResult {
    private String uno;

    private String accesstoken;

    private String refreshtoken;

    private Long expire_date;

    private Long expire_longtime;

    private String type;

    private String username;

    private String icon;

    private int pointamount;

    private String types;

    private String sex; //性别

    private String birthday;  //生日

    private String signature; //签名

    private String play;//正在玩

    private String backpic;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public Long getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(Long expire_date) {
        this.expire_date = expire_date;
    }

    public Long getExpire_longtime() {
        return expire_longtime;
    }

    public void setExpire_longtime(Long expire_longtime) {
        this.expire_longtime = expire_longtime;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getPointamount() {
        return pointamount;
    }

    public void setPointamount(int pointamount) {
        this.pointamount = pointamount;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public String getBackpic() {
        return backpic;
    }

    public void setBackpic(String backpic) {
        this.backpic = backpic;
    }
}
