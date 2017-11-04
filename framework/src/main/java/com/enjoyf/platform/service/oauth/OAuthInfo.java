package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 *  OAuth2
 * User: zhimingli
 * Date: 14-1-3
 * Time: 下午4:23
 * To change this template use File | Settings | File Templates.
 */
public class OAuthInfo implements Serializable{
    private String id;

    private String uno;

    private String access_token;

    private String refresh_token;

    private String app_key;

    private Long expire_date;

    private Long expire_longtime;

    private Date create_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public Long getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(Long expire_date) {
        this.expire_date = expire_date;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Long getExpire_longtime() {
        return expire_longtime;
    }

    public void setExpire_longtime(Long expire_longtime) {
        this.expire_longtime = expire_longtime;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
