package com.enjoyf.platform.service.search.solr;

import com.enjoyf.platform.util.log.GAlerter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-26
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */
public class SolrjUtil {

    public static Map<String, String> getBeanMap(Object bean) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Map<String, String> resultMap = new TreeMap<String, String>();
        try {

            // 获取实体类的所有属性，返回Field数组
            Field[] field = bean.getClass().getDeclaredFields();
            for (int i = 0; i < field.length; i++) { // 遍历所有属性
                String name = field[i].getName(); // 获取属性的名字
                // 获取属性的类型
                String type = field[i].getGenericType().toString();
                if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method method = bean.getClass().getMethod("get" + UpperCaseField(name));
                    String value = (String) method.invoke(bean); // 调用getter方法获取属性值
                    if (value != null) {
                        resultMap.put(name, value);
                    }
                }
            }

        } catch (Exception e) {
            GAlerter.lab("SolrjUtil getBeanMap occur Exception.e:", e);
        }
        return resultMap;
    }

    // 转化字段首字母为大写
    public static String UpperCaseField(String fieldName) {
        fieldName = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase());
        return fieldName;
    }

}
