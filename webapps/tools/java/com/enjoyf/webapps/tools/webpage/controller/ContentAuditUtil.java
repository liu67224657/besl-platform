package com.enjoyf.webapps.tools.webpage.controller;

import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.google.common.base.Strings;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 11-12-5
 * Time: 下午1:26
 * To change this template use File | Settings | File Templates.
 */
public class ContentAuditUtil {
    /**
     * 根据对象的属性名自动给其属性注入值，根据业务逻辑注入的值为起始日期、结束日期
     *
     * @param obj           XXXQueryParam对象
     * @param propertyNames 参数obj对象的属性名数组，形式为：[起始时间, 结束时间]或者[结束时间, 起始时间]
     * @param dates         按照时间顺寻升序排列的数组
     * @param <T>           T
     * @return T
     */
    public static <T> T handleDateParam(T obj, String[] propertyNames, Date[] dates) {
        if (dates == null) {
            return obj;
        }
        try {
            for (int i = 0; i < propertyNames.length; i++) {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyNames[i], obj.getClass());
                Method method = propertyDescriptor.getWriteMethod();
                method.invoke(obj, dates[i]);
            }
        } catch (IntrospectionException e) {
            GAlerter.lab("[Class]ContentAuditUtil [mehtod]handleDateParam caught an Exception: ", e);
        } catch (InvocationTargetException e) {
            GAlerter.lab("[Class]ContentAuditUtil [mehtod]handleDateParam caught an Exception: ", e);
        } catch (IllegalAccessException e) {
            GAlerter.lab("[Class]ContentAuditUtil [mehtod]handleDateParam caught an Exception: ", e);
        }

        return obj;
    }

    /**
     * 将时间字符串进行升序排列 ，如果只输入一个日期字符串，默认起始时间为该日期的前5天
     *
     * @param date1 日期1
     * @param date2 日期2
     * @param days  加n天
     * @return Date[]升序
     */
    public static Date[] sortASC(String date1, String date2, int days) {
        Date[] resultVal = new Date[2];
        Date startDt = DateUtil.StringTodate(date1, DateUtil.DATE_FORMAT);
        Date endDt = DateUtil.StringTodate(date2, DateUtil.DATE_FORMAT);

        if (startDt == null && endDt == null) {
            return null;
        }

        if (startDt == null || endDt == null) {
            String pTime = Strings.isNullOrEmpty(date2) ? date1 : date2;//取一个标准日期，理想状态是一个非空日期
            if (!Strings.isNullOrEmpty(pTime)) {
                endDt = DateUtil.StringTodate(pTime, DateUtil.DATE_FORMAT);
                startDt = DateUtil.dateAdd(endDt, days, DateUtil.DATE_TYPE_DAY);
            }
        } else {
            if (startDt.after(endDt)) {
                Date temp;
                temp = startDt;
                startDt = endDt;
                endDt = temp;
            }
        }
        resultVal[0] = startDt;
        resultVal[1] = DateUtil.StringTodate(DateUtil.DateToString(endDt, DateUtil.DATE_FORMAT) + " 23:59:59",
                DateUtil.DEFAULT_DATE_FORMAT2);

        return resultVal;
    }

    public static String formMapToString(Map<ObjectField, Object> map) {
        Set<Map.Entry<ObjectField, Object>> set = map.entrySet();
        Iterator<Map.Entry<ObjectField, Object>> it = set.iterator();
        String returnValue = "";
        while (it.hasNext()) {
            Map.Entry<ObjectField, Object> entry = it.next();
            ObjectField key = entry.getKey();
            Object value = entry.getValue();
            returnValue += ("," + key.getColumn() + "[" + value + "]");
        }
        return returnValue.substring(1, returnValue.length());
    }
}
