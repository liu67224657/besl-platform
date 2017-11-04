package com.enjoyf.webapps.tools.webpage.controller;

import com.enjoyf.platform.util.DateUtil;
import com.google.common.base.Strings;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-3-14
 * Time: 下午6:50
 * To change this template use File | Settings | File Templates.
 */
public class DateScale {
    private static Map<String, DateScale> map = new HashMap<String, DateScale>();

    public static final DateScale THREE_DAYS = new DateScale("3d");
    public static final DateScale SEVEN_DAYS = new DateScale("7d");
    public static final DateScale A_MONTH = new DateScale("1m");
    public static final DateScale THREE_MONTHS = new DateScale("3m");
    public static final DateScale FOREVER = new DateScale("forever");
    public static final DateScale THREE_MINUTES = new DateScale("3min");
    public static final DateScale THIRTY_MINUTES = new DateScale("30min");

    private String code;

    private DateScale(String code){
        this.code = code;

        map.put(code, this);
    }

    public static DateScale getByCode(String code){
        if (Strings.isNullOrEmpty(code)) {
            return null;
        }

        return map.get(code.toLowerCase());
    }

    public Date getDate(){
        Date returnValue = null;

        if(THREE_DAYS.getCode().equals(code)){
            returnValue = DateUtil.dateAdd(new java.util.Date(), 3, DateUtil.DATE_TYPE_DAY);
        }else if(SEVEN_DAYS.getCode().equals(code)){
            returnValue = DateUtil.dateAdd(new java.util.Date(), 7, DateUtil.DATE_TYPE_DAY);
        }else if(A_MONTH.getCode().equals(code)){
            returnValue = DateUtil.dateAdd(new java.util.Date(), 1, DateUtil.DATE_TYPE_MONTH);
        }else if(THREE_MONTHS.getCode().equals(code)){
            returnValue = DateUtil.dateAdd(new java.util.Date(), 3, DateUtil.DATE_TYPE_MONTH);
        }else if(THREE_MINUTES.getCode().equals(code)){
            returnValue = DateUtil.dateAdd(new Date(), 3, DateUtil.DATE_TYPE_MINUTE);
        }else if(THIRTY_MINUTES.getCode().equals(code)){
            returnValue = DateUtil.dateAdd(new Date(), 3, DateUtil.DATE_TYPE_MINUTE);
        }else {
                returnValue = new java.util.Date(1000*60*60*24);
        }

        return returnValue;
    }

    public String getCode() {
        return code;
    }
}
