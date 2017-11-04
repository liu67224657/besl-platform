package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/12
 * Description:
 */
public class Address implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(Address.class);

    private String contact;  //联系人
    private String phone;           //电话
    private String postcode;        //邮编
    private String province;        //省份
    private String city;            //城市
    private String county;          //县 区
    private String address;         //地址
    private String qq;              //qq或者微信号

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }


    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static Address parse(String jsonStr) {
        Address json = null;
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                json = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<Address>() {
                });

            } catch (IOException e) {
                logger.error("json parse error, jsonStr:" + jsonStr, e);
            }
        }
        return json;
    }


    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public static void main(String[] args) {
        Address json = new Address();
        json.setAddress("liuhaojia");
        json.setCity("海淀");
        json.setProvince("北京");
        json.setPhone("1844444");
        json.setPostcode("100071");
        json.setContact("刘浩");

        System.out.println(json.toJsonStr());

        System.out.println(Address.parse(json.toJsonStr()));
    }
}
