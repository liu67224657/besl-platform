package com.enjoyf.platform.service.event.system;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-28
 * Time: 上午9:29
 * To change this template use File | Settings | File Templates.
 */
public class ProfileSolrjEvent extends SystemEvent {

    private String uno;
    private String searchtext;
    private String sex;
    private String birthday;
    private String appkey;
    private int age;
    private long _version_;

    public ProfileSolrjEvent() {
        super(SystemEventType.PROFILE_SOLRJ_EVENT);
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getSearchtext() {
        return searchtext;
    }

    public void setSearchtext(String searchtext) {
        this.searchtext = searchtext;
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

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long get_version_() {
        return _version_;
    }

    public void set_version_(long _version_) {
        this._version_ = _version_;
    }
}
