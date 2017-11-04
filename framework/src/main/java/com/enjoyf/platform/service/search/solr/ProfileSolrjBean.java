package com.enjoyf.platform.service.search.solr;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-23
 * Time: 下午5:38
 * To change this template use File | Settings | File Templates.
 */
public class ProfileSolrjBean implements Serializable{

    @Field
    private String uno;

    @Field
    private String searchtext;

    @Field
    private String sex;

    @Field
    private String birthday;

    @Field
    private String appkey;

    @Field
    private int age;

    @Field
    private long _version_;

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

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
