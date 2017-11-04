package com.enjoyf.platform.util.json;

import net.sf.json.JSONObject;

/**
 * Created by tonydiao on 2015/5/13.
 */
public class JsonUtil {

    /**
        * 从一个JSON 对象字符格式中得到一个java对象
        *
        * @param jsonString
        * @param beanClass
        * @return
        */
       @SuppressWarnings("unchecked")
       public static <T> T jsonToBean(String jsonString, Class<T> beanClass) {

           JSONObject jsonObject = JSONObject.fromObject(jsonString);
           T bean = (T) JSONObject.toBean(jsonObject, beanClass);

           return bean;

       }

       /**
        * 将java对象转换成json字符串
        *
        * @param bean
        * @return
        */
       public static String beanToJson(Object bean) {

           JSONObject json = JSONObject.fromObject(bean);

           return json.toString();

       }
}
